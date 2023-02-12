/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JTextArea;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Parser
/*     */ {
/*     */   private Token look;
/*  12 */   Table top = null;
/*  13 */   static int label = 0;
/*  14 */   static int blockId = 0;
/*     */   static boolean err = false;
/*  16 */   ArrayList<RenamedId> unPush = new ArrayList<>();
/*     */   public Lexer lex;
/*     */   public JTextArea out;
/*  19 */   String dataseg = "MODEL SMALL\nSTACK 100h\n\nDATASEG\n";
/*  20 */   String codeseg = "ENDS\n\nCODESEG\nMain:\n\tMOV AX, @data\n\tMOV DS, AX\n\n\t";
/*     */   
/*     */   public Parser(String i, JTextArea o) throws IOException {
/*  23 */     this.lex = new Lexer(i); this.out = o; move();
/*     */   }
/*     */   void move() throws IOException {
/*  26 */     this.look = this.lex.scan();
/*     */   }
/*     */   void error(String s) {
/*  29 */     JOptionPane.showMessageDialog(null, "в рядку " + Lexer.line + ": " + s);
/*  30 */     err = true;
/*     */   }
/*     */   
/*     */   void match(int t) throws IOException {
/*  34 */     if (this.look.tag == t) { move(); }
/*  35 */     else { error("синтаксична помилка"); }
/*     */   
/*     */   }
/*     */   public void program() throws IOException {
/*  39 */     block();
/*  40 */     this.out.setText(String.valueOf(this.dataseg) + this.codeseg + "\n\tMOV AX,4C00h\n\tINT 21h\nEND Main\nEND");
/*     */   }
/*     */   
/*     */   void block() throws IOException {
/*  44 */     blockId++;
/*  45 */     match(123);
/*  46 */     Table savedTable = this.top;
/*  47 */     this.top = new Table(this.top);
/*  48 */     for (; this.look.tag != 125; stmt());
/*  49 */     match(125);
/*  50 */     for (int i = this.unPush.size() - 1; i >= 0; i--) {
/*  51 */       RenamedId thisId = this.unPush.get(i);
/*  52 */       if (blockId == thisId.blockNum)
/*  53 */       { this.codeseg = String.valueOf(this.codeseg) + "POP DX\n\tMOV " + thisId.id + ", DL\n\t";
/*  54 */         this.unPush.remove(i); }
/*  55 */       else if (blockId > thisId.blockNum) { break; }
/*     */     
/*  57 */     }  this.top = savedTable;
/*  58 */     blockId--;
/*     */   }
/*     */   
/*     */   void stmt() throws IOException {
/*  62 */     switch (this.look.tag) {
/*     */       case 59:
/*  64 */         move(); return;
/*     */       case 123:
/*  66 */         block(); return;
/*     */       case 256:
/*  68 */         decls(); return;
/*     */       case 261:
/*  70 */         ifConstr(); return;
/*     */       case 259:
/*  72 */         doWhileConstr(); return;
/*     */       case 260:
/*  74 */         whileConstr(); return;
/*     */     } 
/*  76 */     rightConstr();
/*     */   }
/*     */ 
/*     */   
/*     */   void decls() throws IOException {
/*  81 */     while (this.look.tag == 256) {
/*  82 */       Id id = new Id();
/*  83 */       id.type = this.look.toString();
/*  84 */       match(256);
/*  85 */       id.lexeme = this.look.toString();
/*  86 */       boolean initialized = false;
/*  87 */       if (this.top.get(this.look) != null) {
/*  88 */         this.codeseg = String.valueOf(this.codeseg) + "MOV DL, " + id.lexeme + "\n\tPUSH DX\n\t";
/*  89 */         this.unPush.add(new RenamedId(id.lexeme, blockId));
/*  90 */         initialized = true;
/*     */       } 
/*  92 */       this.top.put(this.look, id);
/*  93 */       if (!initialized) this.dataseg = String.valueOf(this.dataseg) + "\t" + this.look.toString() + " DB "; 
/*  94 */       match(257);
/*  95 */       match(61);
/*  96 */       if (!initialized) { this.dataseg = String.valueOf(this.dataseg) + this.look.toString() + "\n"; }
/*  97 */       else { this.codeseg = String.valueOf(this.codeseg) + "MOV DL, " + this.look.toString() + "\n\tMOV " + ((RenamedId)this.unPush.get(this.unPush.size() - 1)).id + ", DL\n\t"; }
/*  98 */        match(258);
/*  99 */       match(59);
/*     */     } 
/*     */   }
/*     */   
/*     */   void ifConstr() throws IOException {
/* 104 */     match(261);
/* 105 */     match(40);
/* 106 */     logicExpr();
/* 107 */     match(41);
/* 108 */     stmt();
/* 109 */     if (this.look.tag == 262) {
/* 110 */       this.codeseg = String.valueOf(this.codeseg) + "JMP L" + (label + 2) + "\n\t\nL" + ++label + ":\n\t";
/* 111 */       move();
/* 112 */       stmt();
/*     */     } 
/* 114 */     this.codeseg = String.valueOf(this.codeseg) + "\nL" + ++label + ":\n\t";
/*     */   }
/*     */   
/*     */   void whileConstr() throws IOException {
/* 118 */     int doLabel = ++label;
/* 119 */     this.codeseg = String.valueOf(this.codeseg) + "\nL" + doLabel + ":\n\t";
/* 120 */     match(260);
/* 121 */     match(40);
/* 122 */     logicExpr();
/* 123 */     match(41);
/* 124 */     stmt();
/* 125 */     this.codeseg = String.valueOf(this.codeseg) + "JMP L" + doLabel + "\n\nL" + ++label + ":\n\t";
/*     */   }
/*     */   
/*     */   void doWhileConstr() throws IOException {
/* 129 */     blockId++;
/* 130 */     int doLabel = ++label;
/* 131 */     this.codeseg = String.valueOf(this.codeseg) + "\nL" + doLabel + ":\n\t";
/* 132 */     match(259);
/* 133 */     Table savedTable = this.top;
/* 134 */     this.top = new Table(this.top);
/* 135 */     for (; this.look.tag != 260; stmt());
/* 136 */     match(260);
/* 137 */     match(40);
/* 138 */     logicExpr();
/* 139 */     match(41); int i;
/* 140 */     for (i = this.unPush.size() - 1; i >= 0; i--) {
/* 141 */       RenamedId thisId = this.unPush.get(i);
/* 142 */       if (blockId == thisId.blockNum)
/* 143 */       { this.codeseg = String.valueOf(this.codeseg) + "POP DX\n\tMOV " + thisId.id + ", DL\n\t"; }
/* 144 */       else if (blockId > thisId.blockNum) { break; }
/*     */     
/* 146 */     }  this.codeseg = String.valueOf(this.codeseg) + "JMP L" + doLabel + "\n\nL" + ++label + ":\n\t";
/* 147 */     for (i = this.unPush.size() - 1; i >= 0; i--) {
/* 148 */       RenamedId thisId = this.unPush.get(i);
/* 149 */       if (blockId == thisId.blockNum)
/* 150 */       { this.codeseg = String.valueOf(this.codeseg) + "POP DX\n\tMOV " + thisId.id + ", DL\n\t";
/* 151 */         this.unPush.remove(i); }
/* 152 */       else if (blockId > thisId.blockNum) { break; }
/*     */     
/* 154 */     }  this.top = savedTable;
/* 155 */     blockId--;
/*     */   }
/*     */   
/*     */   void rightConstr() throws IOException {
/* 159 */     Id id = this.top.get(this.look);
/* 160 */     if (id == null) error(String.valueOf(this.look.toString()) + " не оголошена"); 
/* 161 */     if (id.type.equals("const")) {
/* 162 */       error(String.valueOf(this.look.toString()) + " константа, ви не можете змінити її");
/*     */     }
/* 164 */     match(257);
/* 165 */     if (this.look.tag == 266) {
/* 166 */       this.codeseg = String.valueOf(this.codeseg) + "INC " + id.lexeme + "\n\t";
/* 167 */       move(); return;
/*     */     } 
/* 169 */     match(61);
/* 170 */     arithmeticExpr(id);
/* 171 */     this.codeseg = String.valueOf(this.codeseg) + "MOV " + id + ", AL\n\t";
/* 172 */     match(59);
/*     */   }
/*     */ 
/*     */   
/*     */   void arithmeticExpr(Id id) throws IOException {
/* 177 */     String op1 = "", op2 = "", op = "";
/* 178 */     boolean bracket = false;
/* 179 */     op1 = leftArithmeticExpr(id, op1);
/* 180 */     rightArithmeticExpr(id, op, op1, op2, bracket);
/*     */   }
/*     */   
/*     */   String leftArithmeticExpr(Id id, String op1) throws IOException {
/* 184 */     boolean constant = false;
/* 185 */     Id lookId = this.top.get(this.look);
/* 186 */     if (this.look.tag == 258 || this.look.tag == 257)
/* 187 */     { if (this.look.tag == 257) {
/* 188 */         if (lookId == null) error(String.valueOf(this.look.toString()) + " не оголошена"); 
/* 189 */         if (lookId.type.equals("const")) constant = true; 
/*     */       } 
/* 191 */       op1 = this.look.toString(); move(); }
/* 192 */     else if (this.look.tag == 256)
/* 193 */     { error("синтаксична помилка. \nОголошувати змінні в цьому місці не можна!\n Ти мені набрид, прощавай, невдаха!");
/* 194 */       System.exit(0); }
/* 195 */     else { error("синтаксична помилка. Очікується число, змінна або константа"); }
/* 196 */      if (this.look.tag == 266) {
/* 197 */       if (constant) error(String.valueOf(lookId.toString()) + " константа. Її не можна змінювати"); 
/* 198 */       op1 = id.lexeme; this.codeseg = String.valueOf(this.codeseg) + "INC " + op1 + "\n\t"; move();
/* 199 */     }  this.codeseg = String.valueOf(this.codeseg) + "MOV AL, " + op1 + "\n\t";
/* 200 */     return op1;
/*     */   }
/*     */   
/*     */   void rightArithmeticExpr(Id id, String op, String op1, String op2, boolean bracket) throws IOException {
/* 204 */     while (this.look.tag == 43 || this.look.tag == 45) {
/* 205 */       switch (this.look.tag) {
/*     */         case 43:
/* 207 */           op = "+"; move(); break;
/*     */         case 45:
/* 209 */           op = "-"; move(); break;
/*     */       } 
/* 211 */       boolean constant = false;
/* 212 */       Id lookId = this.top.get(this.look);
/* 213 */       if (this.look.tag == 258 || this.look.tag == 257)
/* 214 */       { if (this.look.tag == 257) {
/* 215 */           if (lookId == null) error(String.valueOf(this.look.toString()) + " не оголошена"); 
/* 216 */           if (lookId.type.equals("const")) constant = true; 
/*     */         } 
/* 218 */         op2 = this.look.toString(); move(); }
/* 219 */       else if (this.look.tag == 40)
/* 220 */       { op = brackets(op, id); bracket = true; }
/* 221 */       else if (this.look.tag == 256)
/* 222 */       { error("синтаксична помилка. Не можна оголошувати змінні в цьому місці!"); }
/* 223 */       else { error("синтаксична помилка. Очікується дужка, число, змінна або константа"); }
/* 224 */        if (this.look.tag == 266) {
/* 225 */         if (constant) error(String.valueOf(lookId.toString()) + " константа. Її не можна змінювати!"); 
/* 226 */         op1 = id.lexeme; this.codeseg = String.valueOf(this.codeseg) + "INC " + op1 + "\n\t"; move();
/*     */       }  String str;
/* 228 */       switch ((str = op).hashCode()) { case 43: if (!str.equals("+"))
/*     */             continue; 
/* 230 */           if (!bracket)
/* 231 */           { this.codeseg = String.valueOf(this.codeseg) + "MOV BL, " + op2 + "\n\t"; }
/* 232 */           else { bracket = false; }
/* 233 */            this.codeseg = String.valueOf(this.codeseg) + "ADD AL, BL\n\t";
/*     */         case 45: if (!str.equals("-"))
/* 235 */             continue;  if (!bracket)
/* 236 */           { this.codeseg = String.valueOf(this.codeseg) + "MOV BL, " + op2 + "\n\t"; }
/* 237 */           else { bracket = false; }
/* 238 */            this.codeseg = String.valueOf(this.codeseg) + "SUB AL, BL\n\t"; }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   String brackets(String op, Id id) throws IOException {
/* 244 */     String op1 = "", op2 = "", oldOp = op;
/* 245 */     boolean bracket = false;
/* 246 */     this.codeseg = String.valueOf(this.codeseg) + "PUSH AX\n\t"; move();
/* 247 */     op1 = leftArithmeticExpr(id, op1);
/* 248 */     rightArithmeticExpr(id, op, op1, op2, bracket);
/* 249 */     while (this.look.tag != 41)
/* 250 */       rightArithmeticExpr(id, op, op1, op2, bracket); 
/* 251 */     move(); this.codeseg = String.valueOf(this.codeseg) + "MOV BL, AL\n\tPOP AX\n\t"; return oldOp;
/*     */   }
/*     */   
/*     */   void logicExpr() throws IOException {
/* 255 */     compareExpr();
/* 256 */     while (this.look.tag == 265 || this.look.tag == 264) {
/* 257 */       if (this.look.tag == 265) {
/* 258 */         this.codeseg = String.valueOf(this.codeseg) + "\n\tJMP L" + (label + 2) + "\nL" + ++label + ":\n\t";
/* 259 */         move(); label++; compareExpr();
/* 260 */         this.codeseg = String.valueOf(this.codeseg) + "\nL" + label + ":\n\t"; continue;
/*     */       } 
/* 262 */       this.codeseg = String.valueOf(this.codeseg) + "\n\t";
/* 263 */       move(); compareExpr();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void compareExpr() throws IOException {
/* 269 */     String op1 = "", op2 = "";
/* 270 */     String op = "";
/* 271 */     boolean not = false;
/* 272 */     if (this.look.tag == 33) { not = true; move(); }
/* 273 */      if (this.look.tag == 258 || this.look.tag == 257)
/* 274 */     { if (this.look.tag == 257) {
/* 275 */         Id lookId = this.top.get(this.look);
/* 276 */         if (lookId == null) error(String.valueOf(this.look.toString()) + " не оголошена"); 
/*     */       } 
/* 278 */       op1 = this.look.toString(); move(); }
/* 279 */     else { error("синтаксична помилка. Очікується число, змінна або константа"); }
/* 280 */      switch (this.look.tag) {
/*     */       case 62:
/* 282 */         if (not) { op = "<="; } else { op = ">"; }
/* 283 */          move(); break;
/*     */       case 60:
/* 285 */         if (not) { op = ">="; } else { op = "<"; }
/* 286 */          move(); break;
/*     */       case 263:
/* 288 */         if (not) { op = "!="; } else { op = "=="; }
/* 289 */          move(); break;
/*     */       default:
/* 291 */         error("синтаксична помилка. Очікується операція порівняння"); break;
/*     */     } 
/* 293 */     if (this.look.tag == 258 || this.look.tag == 257)
/* 294 */     { if (this.look.tag == 257) {
/* 295 */         Id lookId = this.top.get(this.look);
/* 296 */         if (lookId == null) error(String.valueOf(this.look.toString()) + " не оголошена"); 
/*     */       } 
/* 298 */       op2 = this.look.toString(); move(); }
/* 299 */     else { error("синтаксична помилка. Очікується число, змінна або константа"); }
/* 300 */      this.codeseg = String.valueOf(this.codeseg) + "MOV AL, " + op1 + "\n\tCMP AL, " + op2 + "\n\t"; String str1;
/* 301 */     switch ((str1 = op).hashCode()) { case 60: if (!str1.equals("<")) {
/*     */           break;
/*     */         }
/*     */         
/* 305 */         this.codeseg = String.valueOf(this.codeseg) + "JNL L" + (label + 1) + "\n\t"; break;
/*     */       case 62: if (!str1.equals(">"))
/*     */           break;  this.codeseg = String.valueOf(this.codeseg) + "JNG L" + (label + 1) + "\n\t"; break;
/*     */       case 1084: if (!str1.equals("!="))
/* 309 */           break;  this.codeseg = String.valueOf(this.codeseg) + "JE L" + (label + 1) + "\n\t"; break;
/*     */       case 1921:
/*     */         if (!str1.equals("<="))
/*     */           break; 
/* 313 */         this.codeseg = String.valueOf(this.codeseg) + "JNLE L" + (label + 1) + "\n\t";
/*     */         break;
/*     */       case 1952:
/*     */         if (!str1.equals("=="))
/*     */           break; 
/*     */         this.codeseg = String.valueOf(this.codeseg) + "JNE L" + (label + 1) + "\n\t";
/*     */         break;
/*     */       case 1983:
/*     */         if (!str1.equals(">="))
/*     */           break; 
/*     */         this.codeseg = String.valueOf(this.codeseg) + "JNGE L" + (label + 1) + "\n\t";
/*     */         break; }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\User\Downloads\YYC.jar!\Parser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */