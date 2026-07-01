
abstract class Car {
    
    protected String brand;
    protected String model;
    protected boolean isEngineOn;
    protected int currentSpeed;

    public Car(String brand, String model) {
        this.brand = brand;
        this.model = model;
        isEngineOn = false;
        currentSpeed = 0;
    }

    // Polymorphsim
    abstract public void accelerate();

    public void startEngine() {
        isEngineOn = true;
        System.out.println("The car has started");
    }

    public void brake() {
        if(!isEngineOn) {
            System.out.println(brand + " " + model + ":" +"Engine is off. Brake failed");
        }

        currentSpeed = 0;

        System.out.println(brand + " " + model + ":" +"Car stopped after applying brake");
    }

    public void stopEngine() {
        if(!isEngineOn) {
            System.out.println(brand + " " + model + ":" +"Engine is off. Stop Engine is failed");
        }

        isEngineOn = false;
        System.out.println(brand + " " + model + " Engine is off now");
    }
}

class ElectricCar extends Car {

    private int batteryLevel;

    public ElectricCar(String brand, String model) {
        super(brand, model);
    }

    public void chargeBattery() {
        batteryLevel = 100;
        System.out.println(brand + " " + model + ": Battery is fully charged 100%");
    }

    @Override
    public void accelerate() {
        currentSpeed+=20;
        System.out.println(brand + " " + model + ": Electric Car is accelarated to " + currentSpeed);
    }
}

class ManualCar extends Car {

    private int currentGear;

    public ManualCar(String brand, String model) {
        super(brand, model); // Calls parent class constrcutor
    }

    public void shiftGear(int gear) {
        currentGear = gear;
        System.out.println(brand + " " + model + ": Shifted to gear " + currentGear);
    }

    @Override
    public void accelerate() {
        currentSpeed+=50;
        System.out.println(brand + " " + model + ": Manual is accelarated to " + currentSpeed);
    }
}


public class Polymorphism {
    public static void main(String[] args) {
        
        ManualCar car = new ManualCar("Ford", "Mustang");

        car.startEngine();
        car.shiftGear(1);
        car.accelerate();
        car.shiftGear(2);
        car.accelerate();
        car.brake();
        car.stopEngine();

        ElectricCar car2 = new ElectricCar("Tesla", "x");

        car2.startEngine();
        car2.chargeBattery();
        car2.accelerate();
        car2.accelerate();
        car2.brake();
        car2.stopEngine();
    }
}
