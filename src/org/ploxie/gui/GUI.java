package org.ploxie.gui;

import org.ploxie.pathfinder.web.WebLoader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

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

        setSize(800, 600);
        setTitle("Web Editor");

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(e -> saveAs());
        JMenuItem load = new JMenuItem("Open");
        load.addActionListener(e -> load());

        file.add(save);
        file.add(load);
        menuBar.add(file);

        mapViewer = new WorldMapViewer("https://cdn.runescape.com/assets/img/external/oldschool/2019/newsposts/2019-01-10/osrs_world_map_jan4_2019.png");

        setJMenuBar(menuBar);
        add(mapViewer);
    }


    private void load() {
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
    }

    public static void main(String[] args) {
        GUI gui = new GUI();

        gui.setVisible(true);
    }

}
