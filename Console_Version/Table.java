/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ public class Table
/*    */ {
/*    */   private HashMap<Token, Id> table;
/*    */   protected Table prev;
/*    */   
/*    */   public Table(Table n) {
/* 10 */     this.table = new HashMap<>(); this.prev = n;
/*    */   }
/*    */   public void put(Token w, Id i) {
/* 13 */     this.table.put(w, i);
/*    */   }
/*    */ 
/*    */   
/*    */   public Id get(Token w) {
/* 18 */     for (Table e = this; e != null; e = e.prev) {
/* 19 */       Id found = e.table.get(w);
/* 20 */       if (found != null) return found; 
/*    */     } 
/* 22 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\User\Downloads\YYC.jar!\Table.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */