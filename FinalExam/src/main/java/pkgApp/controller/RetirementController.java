package pkgApp.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.poi.ss.formula.functions.FinanceLib;

import com.sun.prism.paint.Color;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import javafx.beans.value.*;

import pkgApp.RetirementApp;
import pkgCore.Retirement;

public class RetirementController implements Initializable {

	private RetirementApp mainApp = null;
	@FXML
	private TextField txtSaveEachMonth;
	@FXML
	private TextField txtYearsToWork;
	@FXML
	private TextField txtAnnualReturnWorking;
	@FXML
	private TextField txtWhatYouNeedToSave;
	@FXML
	private TextField txtYearsRetired;
	@FXML
	private TextField txtAnnualReturnRetired;
	@FXML
	private TextField txtRequiredIncome;
	@FXML
	private TextField txtMonthlySSI;

	private HashMap<TextField, String> hmTextFieldRegEx = new HashMap<TextField, String>();

	public RetirementApp getMainApp() {
		return mainApp;
	}

	public void setMainApp(RetirementApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Adding an entry in the hashmap for each TextField control I want to validate
		// with a regular expression
		// "\\d*?" - means any decimal number
		// "\\d*(\\.\\d*)?" means any decimal, then optionally a period (.), then
		// decmial
		
		hmTextFieldRegEx.put(txtYearsToWork, "(([123]?\\d)|(40))");
		hmTextFieldRegEx.put(txtAnnualReturnWorking, "0(.(1|(0(\\d)+)))?");
		hmTextFieldRegEx.put(txtYearsRetired, "(([1]?\\d)|(20))");
		hmTextFieldRegEx.put(txtAnnualReturnRetired, "0(.(1|(0(\\d)+)))?");
		hmTextFieldRegEx.put(txtRequiredIncome, "(264[23456789])|(26[56789]\\d)|(2[789]\\d\\d)|([3456789]\\d\\d\\d)|(10000)");
		hmTextFieldRegEx.put(txtMonthlySSI, "(2[012345]\\d\\d)|(26[0123]\\d)|(264[012])|([01]?\\d?\\d?\\d)");
		
		// Check out these pages (how to validate controls):
		// https://stackoverflow.com/questions/30935279/javafx-input-validation-textfield
		// https://stackoverflow.com/questions/40485521/javafx-textfield-validation-decimal-value?rq=1
		// https://stackoverflow.com/questions/8381374/how-to-implement-a-numberfield-in-javafx-2-0
		// There are some examples on how to validate / check format

		Iterator it = hmTextFieldRegEx.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			TextField txtField = (TextField) pair.getKey();
			String strRegEx = (String) pair.getValue();

			txtField.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					// If newPropertyValue = true, then the field HAS FOCUS
					// If newPropertyValue = false, then field HAS LOST FOCUS
					if (!newPropertyValue) {
						if (!txtField.getText().matches(strRegEx)) {
							txtField.setText("");
							txtField.requestFocus();
						}
					}
				}
			});
		}

		//
		// TODO: Validate Working Annual Return %, accept only numbers and decimals
		// TODO: Validate Years retired, accepted only decimals
		// TODO: Validate Retired Annual Return %, accept only numbers and deciamls
		// TODO: Validate Required Income, accept only decimals
		// TODO: Validate Monthly SSI, accept only decimals
	}

	@FXML
    public void btnClear(ActionEvent event) {
        System.out.println("Clear pressed");

        // disable read-only controls
        txtSaveEachMonth.setDisable(true);
        txtWhatYouNeedToSave.setDisable(true);

        // Clear, enable txtYearsToWork
        txtYearsToWork.clear();
        txtYearsToWork.setDisable(false);


        Iterator it = hmTextFieldRegEx.entrySet().iterator();
            while (it.hasNext())
            {
                Map.Entry pair = (Map.Entry)it.next();
                TextField txtField = (TextField) pair.getKey();

                txtField.clear();
                txtField.setDisable(false);
            }
	}

            @FXML
            public void btnCalculate() {

                System.out.println("calculating");

                txtSaveEachMonth.setDisable(false);
                txtWhatYouNeedToSave.setDisable(false);

                int yearsToWork = Integer.parseInt(txtYearsToWork.getText());
                int yearsRetired = Integer.parseInt(txtYearsRetired.getText());
                double annualReturnWorking = Double.parseDouble(txtAnnualReturnWorking.getText());
                double annualReturnRetired = Double.parseDouble(txtAnnualReturnRetired.getText());
                double monthlySSI = Double.parseDouble(txtMonthlySSI.getText());
                double requiredIncome = Double.parseDouble(txtRequiredIncome.getText());

                Retirement ret = new Retirement(yearsToWork, annualReturnWorking, yearsRetired, 
                        annualReturnRetired, requiredIncome, monthlySSI);

                // TODO: Calculate txtWhatYouNeedToSave value...
                    double toSave = ret.TotalAmountToSave();
                    txtWhatYouNeedToSave.setText("" + toSave);

                // TODO: Then calculate txtSaveEachMonth, using amount from txtWhatYouNeedToSave
                // as input
                    double toSaveEachMonth = Math.abs(ret.MonthlySavings());
                    txtSaveEachMonth.setText("" + toSaveEachMonth);
            }
}

