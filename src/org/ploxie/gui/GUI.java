package org.ploxie.gui;

import javax.swing.*;

import org.pushingpixels.substance.api.skin.*;

public class GUI extends JFrame {

    private WorldMapViewer mapViewer;

    public GUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        setResizable(false);

        setSize(800, 800);
        setTitle("Web Editor");
        setResizable(true);

        /*JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(e -> saveAs());
        JMenuItem load = new JMenuItem("Open");
        load.addActionListener(e -> load());*/

        /*file.add(save);
        file.add(load);
        menuBar.add(file);

        mapViewer = new WorldMapViewer("https://cdn.runescape.com/assets/img/external/oldschool/2019/newsposts/2019-01-10/osrs_world_map_jan4_2019.png");

        setJMenuBar(menuBar);
        add(mapViewer);*/

        /*JPanel panel = new JPanel(){
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                g.drawImage(grid.images[0][1], 0,0, grid.images[0][1].getWidth(), grid.images[0][1].getHeight(), null);
            }
        };

        add(panel);*/

        WorldMapViewer viewer = new WorldMapViewer(0,0);
        add(viewer);


        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
                SwingUtilities.updateComponentTreeUI(this);
            } catch (Exception e) {

            }
        });

    }


    /*private void load() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Web Data File (*.web)", "web");
        fileChooser.setFileFilter(filter);
        fileChooser.setSelectedFile(new File("data.web"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            mapViewer.web = WebLoader.loadWeb(file);
        }
    }

    private void saveAs() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Web Data File (*.web)", "web");
        fileChooser.setFileFilter(filter);
        fileChooser.setSelectedFile(new File("data.web"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            WebLoader.saveWebToFile(mapViewer.web, file);
        }
    }*/

    public static void main(String[] args) {
        GUI gui = new GUI();

        gui.setVisible(true);
    }

}
