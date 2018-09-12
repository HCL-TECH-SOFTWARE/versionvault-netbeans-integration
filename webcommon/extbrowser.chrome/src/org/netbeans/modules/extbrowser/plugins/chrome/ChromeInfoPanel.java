/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.netbeans.modules.extbrowser.plugins.chrome;

import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.UIManager;

import org.netbeans.modules.extbrowser.plugins.ExtensionManager.ExtensitionStatus;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 *
 * @author ads
 */
class ChromeInfoPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 5394629966593049098L;
    private static final Logger LOGGER = Logger.getLogger(ChromeInfoPanel.class.getName());


    ChromeInfoPanel(String pluginPath, 
            ExtensitionStatus currentStatus)
    {
        initComponents();

        File file = new File(pluginPath);
        String name = file.getName();
        File parent = file.getParentFile();
        StringBuilder text = new StringBuilder("<html>");                       // NOI18N
        if ( currentStatus == ExtensitionStatus.NEEDS_UPGRADE ){
            text.append(NbBundle.getMessage(ChromeInfoPanel.class, "TXT_RequestUpgrade"));// NOI18N
            text.append(" ");           // NOI18N
        }
        String path = "";
        try {
            path = Utilities.toURI(parent).toURL().toExternalForm();
        }
        catch( MalformedURLException e ){
            LOGGER.log(Level.WARNING, null, e);
        }
        text.append(NbBundle.getMessage(ChromeInfoPanel.class,
                "TXT_PluginIstallationIssue" ,                          // NOI18N
                path, name, Integer.toHexString(UIManager.getDefaults().getColor("nb.errorForeground").getRGB() & 0xffffff))); // NOI18N
        text.append("</html>");         // NOI18N
        myEditorPane.setText(text.toString());
        myEditorPane.setCaretPosition(0);
        // tweak ui (text readability in dark theme + proper font)
        myEditorPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        myEditorPane.setFont(new JLabel().getFont());
        
        myEditorPane.addHyperlinkListener(new LinkListener());
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myScrollPane = new javax.swing.JScrollPane();
        myEditorPane = new javax.swing.JEditorPane();

        myEditorPane.setEditable(false);
        myEditorPane.setContentType("text/html"); // NOI18N
        myEditorPane.setText(org.openide.util.NbBundle.getMessage(ChromeInfoPanel.class, "TXT_PluginIstallationIssue")); // NOI18N
        myScrollPane.setViewportView(myEditorPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(myScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(myScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane myEditorPane;
    private javax.swing.JScrollPane myScrollPane;
    // End of variables declaration//GEN-END:variables
    
}