package com.hfad.SAADApplication;

public class Driver {
    private String DriverFullName, Driver_email, Driver_PhoneNumber, NationalIDDB,
            relativeNumberDB,Driver_password, Driver_BloodType,
            disabled, Driver_Illness, CarIDDriver,CarCompanyDriver,CarColorDriver;

    public Driver(String fullName, String email, String PhoneNumber, String NationalID,
                  String RelativeNumber, String password, String blood, String disabledPerson,
                  String DriverIllness, String CarIDDB, String CarCompany,String CarColor){
        this.DriverFullName=fullName;
        this.Driver_email=email;
        this.Driver_PhoneNumber=PhoneNumber;
        this.NationalIDDB=NationalID;
        this.relativeNumberDB=RelativeNumber;
        this.Driver_password=password;
        this.Driver_BloodType=blood;
        this.disabled=disabledPerson;
        this.Driver_Illness=DriverIllness;
        this.CarIDDriver=CarIDDB;
        this.CarCompanyDriver=CarCompany;
        this.CarColorDriver=CarColor;
    }

    public String getDriverFullName() {
        return DriverFullName;
    }

    public String getDriver_email() {
        return Driver_email;
    }

    public String getEmail() {
        return Driver_email;
    }

    public String getDriver_PhoneNumber() {
        return Driver_PhoneNumber;
    }

    public String getNationalIDDB() {
        return NationalIDDB;
    }

    public String getRelativeNumberDB() {
        return relativeNumberDB;
    }

    public String getDriver_password() {
        return Driver_password;
    }

    public String getDriver_BloodType() {
        return Driver_BloodType;
    }

    public String getDisabled() {
        return disabled;
    }

    public String getDriver_Illness() {
        return Driver_Illness;
    }

    public String getCarIDDriver() {
        return CarIDDriver;
    }

    public String getCarCompanyDriver() {
        return CarCompanyDriver;
    }

    public String getCarColorDriver() {
        return CarColorDriver;
    }

    public void setDriverFullName(String driverFullName) {
        DriverFullName = driverFullName;
    }

    public void setDriver_email(String driver_email) {
        Driver_email = driver_email;
    }

    public void setDriver_PhoneNumber(String driver_PhoneNumber) {
        Driver_PhoneNumber = driver_PhoneNumber;
    }

    public void setNationalIDDB(String nationalIDDB) {
        NationalIDDB = nationalIDDB;
    }

    public void setRelativeNumberDB(String relativeNumberDB) {
        relativeNumberDB = relativeNumberDB;
    }

    public void setDriver_password(String driver_password) {
        Driver_password = driver_password;
    }

    public void setDriver_BloodType(String driver_BloodType) {
        Driver_BloodType = driver_BloodType;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public void setDriver_Illness(String driver_Illness) {
        Driver_Illness = driver_Illness;
    }

    public void setCarIDDriver(String carIDDriver) {
        CarIDDriver = carIDDriver;
    }

    public void setCarCompanyDriver(String carCompanyDriver) {
        CarCompanyDriver = carCompanyDriver;
    }

    public void setCarColorDriver(String carColorDriver) {
        CarColorDriver = carColorDriver;
    }
}