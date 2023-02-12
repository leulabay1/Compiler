/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Image;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ public class Form extends JFrame {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public Form() {
/*  28 */     super("Yip-Yip Compiler");
/*  29 */     setDefaultCloseOperation(3);
/*     */     
/*  31 */     Font font = new Font("Verdana", 0, 10);
/*  32 */     final JTabbedPane tabbedPane = new JTabbedPane();
/*  33 */     tabbedPane.setFont(font);
/*     */     
/*  35 */     final JTextArea inText = new JTextArea();
/*  36 */     JScrollPane in = new JScrollPane(inText);
/*  37 */     final JTextArea outText = new JTextArea();
/*  38 */     outText.setEditable(false);
/*  39 */     JScrollPane out = new JScrollPane(outText);
/*  40 */     tabbedPane.addTab("Сирцевий код", in);
/*  41 */     tabbedPane.addTab("Асемблерний код", out);
/*     */ 
/*     */     
/*  44 */     JPanel content = new JPanel();
/*  45 */     content.setLayout(new BorderLayout());
/*     */     
/*  47 */     JPanel buttons = new JPanel();
/*  48 */     content.add(buttons, "Last");
/*     */     
/*  50 */     JButton run = new JButton("Скомпілювати");
/*  51 */     URL url = getClass().getResource("run.png");
/*  52 */     Image im = Toolkit.getDefaultToolkit().getImage(url);
/*  53 */     run.setIcon(new ImageIcon(im));
/*  54 */     run.setPreferredSize(new Dimension(165, 30));
/*  55 */     run.setFont(new Font("Verdana", 1, 11));
/*  56 */     run.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/*  58 */             Parser parse = null;
/*     */             try {
/*  60 */               Parser.label = 0;
/*  61 */               Lexer.line = 1;
/*  62 */               outText.setText("Виникла помилка!");
/*  63 */               parse = new Parser(inText.getText(), outText);
/*  64 */               parse.program();
/*  65 */               if (!Parser.err) tabbedPane.setSelectedIndex(1); 
/*  66 */               Parser.err = false;
/*  67 */             } catch (IOException iOException) {}
/*     */           }
/*     */         });
/*  70 */     buttons.add(run);
/*     */     
/*  72 */     UIManager.put("FileChooser.openButtonText", "Відкрити");
/*  73 */     UIManager.put("FileChooser.cancelButtonText", "Відмінити");
/*  74 */     UIManager.put("FileChooser.saveButtonText", "Зберегти");
/*  75 */     UIManager.put("FileChooser.saveButtonToolTipText", "Зберегти");
/*  76 */     UIManager.put("FileChooser.openButtonToolTipText", "Відкрити");
/*  77 */     UIManager.put("FileChooser.cancelButtonToolTipText", "Відмінити");
/*  78 */     UIManager.put("FileChooser.lookInLabelText", "Шукати в");
/*  79 */     UIManager.put("FileChooser.lookInLabelText", "Папка");
/*  80 */     UIManager.put("FileChooser.saveInLabelText", "Папка");
/*  81 */     UIManager.put("FileChooser.fileNameLabelText", "Назва файлу");
/*  82 */     UIManager.put("FileChooser.filesOfTypeLabelText", "Тип файлу");
/*  83 */     UIManager.put("FileChooser.upFolderToolTipText", "На один рівень вгору");
/*  84 */     UIManager.put("FileChooser.newFolderToolTipText", "Створити нову папку");
/*  85 */     UIManager.put("FileChooser.listViewButtonToolTipText", "Список");
/*  86 */     UIManager.put("FileChooser.detailsViewButtonToolTipText", "Таблиця");
/*  87 */     UIManager.put("FileChooser.fileNameHeaderText", "Назва");
/*  88 */     UIManager.put("FileChooser.fileSizeHeaderText", "Розмір");
/*  89 */     UIManager.put("FileChooser.fileTypeHeaderText", "Тип");
/*  90 */     UIManager.put("FileChooser.fileDateHeaderText", "Змінений");
/*  91 */     UIManager.put("FileChooser.fileAttrHeaderText", "Атрибути");
/*  92 */     UIManager.put("FileChooser.acceptAllFileFilterText", "Всі файли");
/*     */     
/*  94 */     JButton load = new JButton("Відкрити");
/*  95 */     url = getClass().getResource("load.png");
/*  96 */     im = Toolkit.getDefaultToolkit().getImage(url);
/*  97 */     load.setIcon(new ImageIcon(im));
/*  98 */     load.setPreferredSize(new Dimension(165, 30));
/*  99 */     load.setFont(new Font("Verdana", 1, 11));
/* 100 */     load.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 102 */             JFileChooser fileOpen = new JFileChooser();
/* 103 */             int ret = fileOpen.showDialog(null, "Відкрити");
/* 104 */             if (ret == 0) {
/*     */               
/*     */               try {
/* 107 */                 String str = "";
/* 108 */                 BufferedReader b = new BufferedReader(new FileReader(fileOpen.getSelectedFile()));
/* 109 */                 for (; b.ready(); str = String.valueOf(str) + b.readLine() + "\n");
/* 110 */                 inText.setText(str);
/* 111 */               } catch (Exception exception) {}
/*     */             }
/*     */           }
/*     */         });
/*     */     
/* 116 */     buttons.add(load);
/*     */     
/* 118 */     JButton save = new JButton("Зберегти");
/* 119 */     url = getClass().getResource("save.png");
/* 120 */     im = Toolkit.getDefaultToolkit().getImage(url);
/* 121 */     save.setIcon(new ImageIcon(im));
/* 122 */     save.setPreferredSize(new Dimension(165, 30));
/* 123 */     save.setFont(new Font("Verdana", 1, 11));
/* 124 */     save.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 126 */             JFileChooser fileSave = new JFileChooser();
/* 127 */             if (fileSave.showDialog(null, "Зберегти") == 0) {
/* 128 */               try { Exception exception2, exception1 = null;
/*     */                  }
/*     */               
/* 131 */               catch (IOException iOException) {}
/*     */             }
/*     */           }
/*     */         });
/* 135 */     buttons.add(save);
/*     */     
/* 137 */     content.add(tabbedPane, "Center");
/*     */     
/* 139 */     getContentPane().add(content);
/*     */     
/* 141 */     setPreferredSize(new Dimension(600, 400));
/* 142 */     pack();
/* 143 */     setLocationRelativeTo((Component)null);
/* 144 */     setVisible(true);
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 148 */     SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 150 */             JFrame.setDefaultLookAndFeelDecorated(true);
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\User\Downloads\YYC.jar!\Form.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */