package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }


    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    public void calculateFareCar(){
 
    	//GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        //WHEN
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        
        //THEN
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
//        assertThat(ticket.getPrice()).isEqualTo(Fare.CAR_RATE_PER_HOUR); 
        
    }

    @Test
    public void calculateFareBike(){
        
    	//GIVEN
    	Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        //WHEN
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        
        //THEN
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
//        assertThat(ticket.getPrice()).isEqualTo(Fare.BIKE_RATE_PER_HOUR); 
    }

    @Test
    public void calculateFareUnkownType(){
        
    	//GIVEN
    	Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        //WHEN
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        
        //THEN
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
        
    	//GIVEN 
    	Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        //WHEN
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        
        //THEN
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    /**
     * 45 minutes parking time should give 3/4th parking fare
     */
    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        
    	//GIVEN
    	Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        //WHEN
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        
        //THEN
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
//        assertThat(ticket.getPrice()).isEqualTo((0.75 * Fare.BIKE_RATE_PER_HOUR)); 
    }

    /**
     * 45 minutes parking time should give 3/4th parking fare
     */
    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        //GIVEN
    	Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        //WHEN
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        
        //THEN
        assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
//        assertThat(ticket.getPrice()).isEqualTo((0.75 * Fare.CAR_RATE_PER_HOUR)); 
    }

    /**
     * 24 hours parking time should give 24 * parking fare per hour
     */
    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
        
    	//GIVEN
    	Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (6 * 24 * 60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        //WHEN
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        
        //THEN
        assertEquals( (6 * 24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
//        assertThat(ticket.getPrice()).isEqualTo((6 * 24 * Fare.CAR_RATE_PER_HOUR)); 
    }

    @Test
    public void calculFareCarWithDiscountLessThanOneHour() {
    	
    	//GIVEN
    	Date inTime = new Date(); 
    	inTime.setTime(System.currentTimeMillis() - (45*60*1000));
    	Date outTime = new Date(); 
    	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false); 
    	
    	//WHEN
    	ticket.setInTime(inTime);
    	ticket.setOutTime(outTime);
    	ticket.setParkingSpot(parkingSpot);
    	fareCalculatorService.calculateFare(ticket);
    	fareCalculatorService.calculateFareDiscount(ticket);
    	
    	//THEN
    	assertEquals(((45*Fare.CAR_RATE_PER_MINUTE) - (Fare.CAR_RATE_PER_MINUTE*45*0.05)), ticket.getPrice());
//    	assertThat(ticket.getPrice()).isEqualTo(((45*Fare.CAR_RATE_PER_MINUTE) - (Fare.CAR_RATE_PER_MINUTE*45*0.05))); 
    }
    
    @Test
    public void calculFareCarWithDiscountMoreThanOneHour() {
    	
    	//GIVEN
    	Date inTime = new Date(); 
    	inTime.setTime(System.currentTimeMillis() - (120*60*1000));
    	Date outTime = new Date(); 
    	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false); 
    	
    	//WHEN
    	ticket.setInTime(inTime);
    	ticket.setOutTime(outTime);
    	ticket.setParkingSpot(parkingSpot);
    	fareCalculatorService.calculateFare(ticket);
    	fareCalculatorService.calculateFareDiscount(ticket);
    	
    	//THEN
    	assertEquals(((120*Fare.CAR_RATE_PER_MINUTE) - (Fare.CAR_RATE_PER_MINUTE*120*0.05)), ticket.getPrice());
//    	assertThat(ticket.getPrice()).isEqualTo(((120*Fare.CAR_RATE_PER_MINUTE) - (Fare.CAR_RATE_PER_MINUTE*120*0.05))); 
    }
    
    @Test
    public void calculFareCarWithDiscountMoreThanOneDay() {
    	
    	//GIVEN
    	Date inTime = new Date(); 
    	inTime.setTime(System.currentTimeMillis() - (24*60*60*1000));
    	Date outTime = new Date(); 
    	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false); 
    	
    	//WHEN
    	ticket.setInTime(inTime);
    	ticket.setOutTime(outTime);
    	ticket.setParkingSpot(parkingSpot);
    	fareCalculatorService.calculateFare(ticket);
    	fareCalculatorService.calculateFareDiscount(ticket);
    	
    	//THEN
    	assertEquals(((24*60*Fare.CAR_RATE_PER_MINUTE) - (Fare.CAR_RATE_PER_MINUTE*24*60*0.05)), ticket.getPrice());
//    	assertThat(ticket.getPrice()).isEqualTo(((24*60*Fare.CAR_RATE_PER_MINUTE) - (Fare.CAR_RATE_PER_MINUTE*24*60*0.05))); 
    }
    
    @Test
    public void calculFareBikeWithDiscountMoreThanOneDay() {
    	
    	//GIVEN
    	Date inTime = new Date(); 
    	inTime.setTime(System.currentTimeMillis() - (24*60*60*1000));
    	Date outTime = new Date(); 
    	ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false); 
    	
    	//WHEN
    	ticket.setInTime(inTime);
    	ticket.setOutTime(outTime);
    	ticket.setParkingSpot(parkingSpot);
    	fareCalculatorService.calculateFare(ticket);
    	fareCalculatorService.calculateFareDiscount(ticket);
    	
    	//THEN
    	assertEquals(((24*60*Fare.BIKE_RATE_PER_MINUTE) - (Fare.BIKE_RATE_PER_MINUTE*24*60*0.05)), ticket.getPrice());
//    	assertThat(ticket.getPrice()).isEqualTo(((24*60*Fare.BIKE_RATE_PER_MINUTE) - (Fare.BIKE_RATE_PER_MINUTE*24*60*0.05))); 
    }
    
    @Test
    public void calculFareBikeWithDiscountLessThanOneHour() {
    	//GIVEN
    	Date inTime = new Date(); 
    	inTime.setTime(System.currentTimeMillis() - (45*60*1000));
    	Date outTime = new Date(); 
    	ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false); 
    	
    	//WHEN
    	ticket.setInTime(inTime);
    	ticket.setOutTime(outTime);
    	ticket.setParkingSpot(parkingSpot);
    	fareCalculatorService.calculateFare(ticket);
    	fareCalculatorService.calculateFareDiscount(ticket);
    	
    	//THEN
    	assertEquals(((45*Fare.BIKE_RATE_PER_MINUTE) - (Fare.BIKE_RATE_PER_MINUTE*45*0.05)), ticket.getPrice());
//    	assertThat(ticket.getPrice()).isEqualTo(((45*Fare.BIKE_RATE_PER_MINUTE) - (Fare.BIKE_RATE_PER_MINUTE*45*0.05))); 
    }
    
    @Test
    public void calculFareBikeWithDiscountMoreThanOneHour() {
    	//GIVEN
    	Date inTime = new Date(); 
    	inTime.setTime(System.currentTimeMillis() - (120*60*1000));
    	Date outTime = new Date(); 
    	ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false); 
    	
    	ticket.setInTime(inTime);
    	ticket.setOutTime(outTime);
    	ticket.setParkingSpot(parkingSpot);
    	fareCalculatorService.calculateFare(ticket);
    	
    	
    	//WHEN
    	fareCalculatorService.calculateFareDiscount(ticket);
    	
    	//THEN
    	assertEquals(((120*Fare.BIKE_RATE_PER_MINUTE) - (Fare.BIKE_RATE_PER_MINUTE*120*0.05)), ticket.getPrice());
//    	assertThat(ticket.getPrice()).isEqualTo(((120*Fare.BIKE_RATE_PER_MINUTE) - (Fare.BIKE_RATE_PER_MINUTE*120*0.05)));
    }
    
    @Test 
    public void verifyFormatter() {
    	//GIVEN
    	ticket.setPrice(1.317);
    	//WHEN
    	ticket.setPrice(fareCalculatorService.priceFormatter(ticket));
    	//THEN
    	assertEquals(1.32, ticket.getPrice()); 
//    	assertThat(ticket.getPrice()).isEqualTo(1.32); 
    }
}