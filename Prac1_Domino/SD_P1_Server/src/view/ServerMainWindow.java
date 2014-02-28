/*
 * Server Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */
package view;

import controller.ServerController;
import javax.swing.DefaultListModel;
import model.connection.Constants;

/**
 *
 * @author Pablo
 */
public class ServerMainWindow extends javax.swing.JFrame implements Log.OnLogActionListener{
    private boolean listening;
    private Log log; // The log
    private ServerController controller;
    DefaultListModel connections;
    DefaultListModel games;

     
    public static void main(String[] args) {/*
        if (args.length != 2 ){
            System.err.println("Usage: Server -port <port_num>");
            System.exit(1);
        }
        try{
            int port = Integer.parseInt(args[1]);
        }catch(NumberFormatException ex){
            System.err.println("Port must be a number!!");
            System.exit(1);
        }*/
        
        Constants.PORT = 8080;                                          //Must change later
        ServerMainWindow server = new ServerMainWindow(new Log());      //Createa the server
        
        server.initServer();                                            //Start server
    }   
    
    /**
     * Creates new form ServerMainWindow
     * @param log
     */
    public ServerMainWindow(Log log) {
        // Init GUI Components
        initComponents();
        this.games = new DefaultListModel();
        this.gameList.setModel(games);
        this.connections = new DefaultListModel();  
        this.connectionsList.setModel(connections);
        //
        
        // Init other components
        this.log = log;
        this.listening = false;
        //
    }

    public void welcomeMessage(){
        this.log.writePlain("### ");
        this.log.writePlain("### SOFTWARE DISTRIBUIT P1_Server");
        this.log.writePlain("### Developed by: Pablo Martínez && Xavi Moreno");
        this.log.writePlain("### ");
        this.log.writePlain("### Running on port -port "+Constants.PORT);
        this.log.writePlain("");
    }
    public void initServer(){
        this.log.setActionListener(this);
        this.setVisible(true);
        welcomeMessage();
        this.controller = new ServerController(this.log);
        this.listeningToggleButton.setEnabled(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        logArea = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        connectionsList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        conNumberLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        gameList = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        listeningToggleButton = new javax.swing.JButton();
        clCheckBox = new javax.swing.JCheckBox();
        glCheckBox = new javax.swing.JCheckBox();
        elCheckbox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(718, 376));
        setResizable(false);

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(718, 336));

        logArea.setEditable(false);
        logArea.setColumns(20);
        logArea.setRows(5);
        jScrollPane1.setViewportView(logArea);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Log", jPanel3);

        jScrollPane2.setViewportView(connectionsList);

        jLabel1.setText("Nº Connections:");

        conNumberLabel.setText("0");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(conNumberLabel)
                        .addGap(0, 599, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(conNumberLabel))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Connections", jPanel4);

        jScrollPane3.setViewportView(gameList);

        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Games", jPanel5);

        listeningToggleButton.setText("Start listening");
        listeningToggleButton.setEnabled(false);
        listeningToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listeningToggleButtonActionPerformed(evt);
            }
        });

        clCheckBox.setSelected(true);
        clCheckBox.setText("Connection logs");
        clCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clCheckBoxActionPerformed(evt);
            }
        });

        glCheckBox.setSelected(true);
        glCheckBox.setText("Game logs");
        glCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                glCheckBoxActionPerformed(evt);
            }
        });

        elCheckbox.setSelected(true);
        elCheckbox.setText("Exception logs");
        elCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elCheckboxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(listeningToggleButton)
                .addGap(18, 18, 18)
                .addComponent(clCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(glCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(elCheckbox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(listeningToggleButton)
                    .addComponent(clCheckBox)
                    .addComponent(glCheckBox)
                    .addComponent(elCheckbox))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void listeningToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listeningToggleButtonActionPerformed
        boolean ret;
        if (!listening){
            ret = controller.acceptNewConnections();
        }else{
            ret = controller.rejectNewConnections();
        }
        if (ret){
            listeningToggleButton.setText((listening)? "Start listening": "Stop listening");
            listening = !listening;
        }
        
    }//GEN-LAST:event_listeningToggleButtonActionPerformed

    private void clCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clCheckBoxActionPerformed
        controller.toggleConnections();
    }//GEN-LAST:event_clCheckBoxActionPerformed

    private void glCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_glCheckBoxActionPerformed
        controller.toggleGames();
    }//GEN-LAST:event_glCheckBoxActionPerformed

    private void elCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elCheckboxActionPerformed
        controller.toggleErrors();
    }//GEN-LAST:event_elCheckboxActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox clCheckBox;
    private javax.swing.JLabel conNumberLabel;
    private javax.swing.JList connectionsList;
    private javax.swing.JCheckBox elCheckbox;
    private javax.swing.JList gameList;
    private javax.swing.JCheckBox glCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton listeningToggleButton;
    private javax.swing.JTextArea logArea;
    // End of variables declaration//GEN-END:variables
    
    public void updateConNumber(){
        conNumberLabel.setText(connections.size()+"");
    }
    @Override
    public void onAddLog(String s) {
        this.logArea.append("\n"+s);
        this.logArea.setCaretPosition(this.logArea.getDocument().getLength());
    }

    @Override
    public void onGameCreated(String s) {
        games.addElement(s);
    }

    @Override
    public void onGameDestroyed(String s) {
        games.removeElement(s);
    }

    @Override
    public void onNewConnection(String s) {
        connections.addElement(s);
        updateConNumber();
    }

    @Override
    public void onDisconnect(String s) {
        connections.removeElement(s);
        updateConNumber();
    }
    
}
