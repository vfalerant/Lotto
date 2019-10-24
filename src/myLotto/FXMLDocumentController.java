/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myLotto;

import java.lang.Integer;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;


/**
 *
 * @author felhasználó
 */
public class FXMLDocumentController implements Initializable {
    
    // ebben a listában tároljuk a tippeket
    private HashSet<Integer> osszesTipp = new HashSet<Integer>();
    // ebben a listában tároljuk a kisorsolt számokat
    private HashSet<Integer> kisorsoltSzamok = new HashSet<Integer>();

    
    // a lottószámok intervalluma
    private final int LOTTOMIN = 1;
    private final int LOTTOMAX = 90;
    
    // default border beállítások, hogy vissza tudjuk állítani az eredeti nézetét a keretnek
    private final String DEFAULTBORDERSTYLE = "";
    private final String REDBORDERSTYLE = "-fx-border-color: red;";
    
        
    @FXML
    private TextField tipp1;
    @FXML
    private TextField tipp2;
    @FXML
    private TextField tipp3;
    @FXML
    private TextField tipp4;
    @FXML
    private TextField tipp5;
    @FXML
    private Label sorsolt1;
    @FXML
    private Label sorsolt2;
    @FXML
    private Label sorsolt3;
    @FXML
    private Label sorsolt4;
    @FXML
    private Label sorsolt5;
    @FXML
    private Label eredmenyKijelzo;
    
    @FXML
    private void sorsolas(ActionEvent event) {
        // beállítjuk a TextFieldek default keretét, hogy eltüntessük az esetlegesen megjelölt találat mezőket
        //tipp1.set
        tipp1.setStyle(DEFAULTBORDERSTYLE);
        tipp2.setStyle(DEFAULTBORDERSTYLE);
        tipp3.setStyle(DEFAULTBORDERSTYLE);
        tipp4.setStyle(DEFAULTBORDERSTYLE);
        tipp5.setStyle(DEFAULTBORDERSTYLE);
        // beállítjuk a Labelek default keretét, hogy eltüntessük az esetlegesen megjelölt találat mezőket
        sorsolt1.setStyle(DEFAULTBORDERSTYLE);
        sorsolt2.setStyle(DEFAULTBORDERSTYLE);
        sorsolt3.setStyle(DEFAULTBORDERSTYLE);
        sorsolt4.setStyle(DEFAULTBORDERSTYLE);
        sorsolt5.setStyle(DEFAULTBORDERSTYLE);
        // ellenőrízzük, hogy az adatok megfelelően lettek-e megadva
        if (form_validate()==true) { 
            // kinullázzuk a kisorsolt számok tömbjét
            kisorsoltSzamok.clear();
            // kisorsoljuk az öt számot
            szamokKisorsolasa(kisorsoltSzamok);
            // irassuk ki a sorsolás eredményét a konzolra
            //System.out.println(kisorsoltSzamok);
            // kiírjuk a kisorsolt számokat növekvő sorrendben, ehhez átrakjuk egy tömbbe
            Integer[] listKisorsoltSzamok = new Integer[kisorsoltSzamok.size()];
            kisorsoltSzamok.toArray(listKisorsoltSzamok);
            Arrays.sort(listKisorsoltSzamok);
            //System.out.println(listKisorsoltSzamok[0].toString());
            sorsolt1.setText(listKisorsoltSzamok[0].toString());
            sorsolt2.setText(listKisorsoltSzamok[1].toString());
            sorsolt3.setText(listKisorsoltSzamok[2].toString());
            sorsolt4.setText(listKisorsoltSzamok[3].toString());
            sorsolt5.setText(listKisorsoltSzamok[4].toString());
            // a sorsolás eredményének kiíratása
            String talalatokSzamaSzoveg="";
            Integer[] osszesTalalat = talalatokOsszesitese(osszesTipp, kisorsoltSzamok);
            switch (osszesTalalat.length){
                case 0:
                    talalatokSzamaSzoveg = "Sajnos ezúttal egy találatot sem értél el!";
                    break;
                case 1:
                    talalatokSzamaSzoveg = "Ez csak egy találat, még nem fizetne!";
                    break;
                case 2:
                    talalatokSzamaSzoveg = "Két találatod van, ez már fizetne a lottón!";
                    break;
                case 3:
                    talalatokSzamaSzoveg = "Három találat, nagyon ritka eset!";
                    break;
                case 4:
                    talalatokSzamaSzoveg = "Négyes találat, ez már szinte a lehetetlen kategória!";
                    break;
                case 5:
                    talalatokSzamaSzoveg = "Hihetetlen, ötös találatot értél el!";
                    break;
            }
            // pirosítsuk ki az eltalált mezők keretét
            if (osszesTalalat.length>0) {
                talalatokMegjelolese(osszesTalalat, tipp1);
                talalatokMegjelolese(osszesTalalat, tipp2);
                talalatokMegjelolese(osszesTalalat, tipp3);
                talalatokMegjelolese(osszesTalalat, tipp4);
                talalatokMegjelolese(osszesTalalat, tipp5);
                talalatokMegjelolese(osszesTalalat, sorsolt1);
                talalatokMegjelolese(osszesTalalat, sorsolt2);
                talalatokMegjelolese(osszesTalalat, sorsolt3);
                talalatokMegjelolese(osszesTalalat, sorsolt4);
                talalatokMegjelolese(osszesTalalat, sorsolt5);
            }
            // a szöveg megjelenítése
            eredmenyKijelzo.setText(talalatokSzamaSzoveg);
        }
             
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    // vizsgáljuk meg, hogy a megadott számok megfelelően vannak-e kitöltve a képernyőn
    private boolean form_validate(){
        // vizsgáljuk meg, hogy mind az öt mező ki van-e töltve
        if (!isTippFilled(tipp1) || !isTippFilled(tipp2) || !isTippFilled(tipp3) || !isTippFilled(tipp4) || !isTippFilled(tipp5)) 
            return false;
        
        // vizsgáljuk meg, hogy a megadott adatok egész számok-e 1 és 90 között
        if (!isTippInteger(tipp1) || !isTippInteger(tipp2) || !isTippInteger(tipp3) || !isTippInteger(tipp4) || !isTippInteger(tipp5))
            return false;
        
        // vizsgáljuk meg, hogy a számok között nincs duplikátum
        osszesTipp.add(Integer.valueOf(tipp1.getText()));
        osszesTipp.add(Integer.valueOf(tipp2.getText()));
        osszesTipp.add(Integer.valueOf(tipp3.getText()));
        osszesTipp.add(Integer.valueOf(tipp4.getText()));
        osszesTipp.add(Integer.valueOf(tipp5.getText()));
        if (osszesTipp.size()!=5){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Nem jó számokat adott meg");
            alert.setHeaderText("A megadott számoknak mind különbözőnek kell lennie!");
            alert.showAndWait();
            tipp1.requestFocus();
            return false;
        }
        
       return true;
    }
    
    // vizsgáljuk meg, hogy ki van-e töltve a mező
    private boolean isTippFilled(TextField tf){
            if (tf.getLength()==0) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Nincs  minden kötelező adat kitöltve");
                alert.setHeaderText("Kötelező öt számot megadni 1 és 90 között!");
                alert.showAndWait();
                tf.requestFocus();
                return false;
            }
        return true;
    }
    
    // megvizsgáljuk, hogy 1 és 90 közötti egész számot tartalmaz-e a mező
    private boolean isTippInteger(TextField tf){
        try {
            Integer value = Integer.parseInt(tf.getText());
            if (value<LOTTOMIN || value>LOTTOMAX) throw new NumberFormatException("");
            // visszaírjuk a számot a mezőbe, hogy eltüntessük az esetleges vezető nullákat pl. 00001
            tf.setText(Integer.toString(value));
        }
        catch(NumberFormatException e){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Nem jó számokat adott meg");
            alert.setHeaderText("A megadott számoknak 1 és 90 közötti egész számoknak kell lenniük!");
            alert.showAndWait();
            tf.requestFocus();
            return false;
        }
        return true;
    }
    
    // kisorsoljuk az öt számot
    private void szamokKisorsolasa(HashSet<Integer> hs){
        if (hs.size()<5) {
            hs.add(new Random().nextInt((LOTTOMAX - LOTTOMIN) + 1) + LOTTOMIN);
            szamokKisorsolasa(hs);
        }
        
    }
    
    // talalatok összesítése
    private Integer[] talalatokOsszesitese(HashSet<Integer> tippek, HashSet<Integer> sorsoltak){
        // kiolvassuk tömbbe a két HashSet metszetét a retainAll() függvénnyel
        tippek.retainAll(sorsoltak);
        
        //convert to array
        Integer[] intersection = {};
        intersection = tippek.toArray(intersection);
     
        //System.out.println(Arrays.toString(intersection));
        //System.out.println("Intersection lenght: " + intersection.length);
        return intersection;
    }
    
    // bekeretezzük pirossal az eltalált számokat a tippek közül
    private void talalatokMegjelolese(Integer[] talalatok, TextField tipp){
        for(int i : talalatok){
            if (i == Integer.valueOf(tipp.getText()))
                    tipp.setStyle(REDBORDERSTYLE);
        }
    }

    // bekeretezzük pirossal az eltalált számokat a kisorsolt számok közül
    private void talalatokMegjelolese(Integer[] talalatok, Label sorsolt){
        for(int i : talalatok){
            if (i == Integer.valueOf(sorsolt.getText()))
                    sorsolt.setStyle(REDBORDERSTYLE);
        }
        Integer a=2;
        valami(a);
    }
    
    private void valami(int i){
        if (i>0) System.out.println("szuper");
    }
    
}

