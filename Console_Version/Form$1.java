/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import java.io.IOException;
/*    */ import javax.swing.JTabbedPane;
/*    */ import javax.swing.JTextArea;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements ActionListener
/*    */ {
/*    */   public void actionPerformed(ActionEvent e) {
/* 58 */     Parser parse = null;
/*    */     try {
/* 60 */       Parser.label = 0;
/* 61 */       Lexer.line = 1;
/* 62 */       outText.setText("Виникла помилка!");
/* 63 */       parse = new Parser(inText.getText(), outText);
/* 64 */       parse.program();
/* 65 */       if (!Parser.err) tabbedPane.setSelectedIndex(1); 
/* 66 */       Parser.err = false;
/* 67 */     } catch (IOException iOException) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\User\Downloads\YYC.jar!\Form$1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */