package controller;

import java.util.Scanner;

import model.Nurse;
import ui.NurseMenuUI;

public class NurseController extends BaseController<NurseMenuUI> {
    private Nurse nurse;

    public NurseController(
        Scanner scanner,
        Nurse nurse
    ){
        super(new NurseMenuUI(), scanner);
        this.nurse = nurse;
    }

    public void handleUserInput(){
        while(true){
            menu.printOptions();
            String choice = scanner.nextLine();

            if(choice.equals("1")){

            }
            else if(choice.equals("2")){
                
            }
            else if(choice.equals("3")){
                super.handleLogout(nurse);
                break;
            } else {
                super.invalidOption();
            }
        }
    }
}
