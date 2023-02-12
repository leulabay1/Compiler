/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JTextArea;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   implements ActionListener
/*     */ {
/*     */   public void actionPerformed(ActionEvent e) {
/* 102 */     JFileChooser fileOpen = new JFileChooser();
/* 103 */     int ret = fileOpen.showDialog(null, "Відкрити");
/* 104 */     if (ret == 0)
/*     */       
/*     */       try {
/* 107 */         String str = "";
/* 108 */         BufferedReader b = new BufferedReader(new FileReader(fileOpen.getSelectedFile()));
/* 109 */         for (; b.ready(); str = String.valueOf(str) + b.readLine() + "\n");
/* 110 */         inText.setText(str);
/* 111 */       } catch (Exception exception) {} 
/*     */   }
/*     */ }


/* Location:              C:\Users\User\Downloads\YYC.jar!\Form$2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */