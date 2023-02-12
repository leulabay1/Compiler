/*    */ public class Word
/*    */   extends Token {
/*  3 */   public String lexeme = ""; public Word(String s, int tag) {
/*  4 */     super(tag); this.lexeme = s;
/*    */   } public String toString() {
/*  6 */     return this.lexeme;
/*    */   }
/*    */ 
/*    */   
/* 10 */   public static final Word and = new Word("&&", 264), or = new Word("||", 265);
/* 11 */   public static final Word eq = new Word("==", 263), inc = new Word("++", 266);
/* 12 */   public static final Word Var = new Word("var", 256), Const = new Word("const", 256);
/*    */ }


/* Location:              C:\Users\User\Downloads\YYC.jar!\Word.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */