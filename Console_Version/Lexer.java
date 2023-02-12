/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class Lexer {
/*   5 */   public static int line = 1;
/*   6 */   char peek = ' ';
/*   7 */   HashMap<String, Word> words = new HashMap<>();
/*     */   public InputStream in;
/*     */   
/*     */   void reserve(Word w) {
/*  11 */     this.words.put(w.lexeme, w);
/*     */   }
/*     */   public Lexer(String i) throws UnsupportedEncodingException {
/*  14 */     byte[] bytes = i.getBytes("UTF-8");
/*  15 */     this.in = new ByteArrayInputStream(bytes);
/*     */ 
/*     */ 
/*     */     
/*  19 */     reserve(new Word("if", 261));
/*  20 */     reserve(new Word("else", 262));
/*  21 */     reserve(new Word("while", 260));
/*  22 */     reserve(new Word("do", 259));
/*  23 */     reserve(Word.Var); reserve(Word.Const);
/*     */   }
/*     */ 
/*     */   
/*     */   void readch() throws IOException {
/*  28 */     this.peek = (char)this.in.read();
/*     */   }
/*     */ 
/*     */   
/*     */   boolean readch(char c) throws IOException {
/*  33 */     readch();
/*  34 */     if (this.peek != c) return false; 
/*  35 */     this.peek = ' ';
/*  36 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Token scan() throws IOException {
/*  41 */     for (;; readch()) {
/*     */ 
/*     */       
/*  44 */       if (this.peek != ' ' && this.peek != '\t' && this.peek != '\r') {
/*  45 */         if (this.peek == '\n') { line++; }
/*     */         
/*  47 */         else if (this.peek == '/')
/*  48 */         { if (readch('/')) {
/*  49 */             for (; this.peek != '\n'; readch());
/*  50 */             line++;
/*     */           }  }
/*     */         else
/*     */         { break; }
/*     */       
/*     */       }
/*     */     } 
/*     */     
/*  58 */     switch (this.peek) {
/*     */       case '&':
/*  60 */         return readch('&') ? Word.and : new Token(38);
/*     */       case '|':
/*  62 */         return readch('|') ? Word.or : new Token(124);
/*     */       case '=':
/*  64 */         return readch('=') ? Word.eq : new Token(61);
/*     */       case '+':
/*  66 */         return readch('+') ? Word.inc : new Token(43);
/*     */     } 
/*     */ 
/*     */     
/*  70 */     if (Character.isDigit(this.peek)) {
/*  71 */       int v = 0; while (true) {
/*  72 */         v = 10 * v + Character.digit(this.peek, 10); readch(); if (!Character.isDigit(this.peek)) {
/*  73 */           return new Num(v);
/*     */         }
/*     */       } 
/*     */     } 
/*  77 */     if (Character.isLetter(this.peek)) {
/*  78 */       StringBuffer b = new StringBuffer(); while (true) {
/*  79 */         b.append(this.peek); readch(); if (!Character.isLetterOrDigit(this.peek)) {
/*  80 */           String s = b.toString();
/*     */           
/*  82 */           if (s.charAt(0) == 'b') {
/*  83 */             boolean isBin = true;
/*  84 */             for (int i = 1; i < s.length(); i++) {
/*  85 */               if (s.charAt(i) != '0' && s.charAt(i) != '1') {
/*  86 */                 isBin = false; break;
/*     */               } 
/*     */             } 
/*  89 */             if (isBin) {
/*  90 */               return new Num(Integer.parseInt(s.substring(1), 2));
/*     */             }
/*     */           } 
/*     */ 
/*     */           
/*  95 */           Word w = this.words.get(s);
/*  96 */           if (w != null) return w; 
/*  97 */           w = new Word(s, 257);
/*  98 */           this.words.put(s, w);
/*  99 */           return w;
/*     */         } 
/*     */       } 
/*     */     } 
/* 103 */     Token tok = new Token(this.peek); this.peek = ' ';
/* 104 */     return tok;
/*     */   }
/*     */ }


/* Location:              C:\Users\User\Downloads\YYC.jar!\Lexer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */