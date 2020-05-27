package taxipark

import kotlin.math.ceil
import kotlin.math.floor

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        allDrivers.filter {
        var fakeDriver = true
        for(trip in trips) {
            if(trip.driver.name.equals(it.name)) {
                fakeDriver = false
                break;
            }
        }
        fakeDriver
    }.toSet()

//allDrivers.filter { d -> trips.none {it.driver ==d } }.toSet()
//allDrivers - trips.map { it.driver } // Efficient

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        allPassengers.filter {
            var countTrips = 0
            for(trip in trips) {
                if(it in trip.passengers) {
                    countTrips++
                }
            }
            countTrips >= minTrips
        }.toSet()

//allPassengers
//    .filter { p ->
//        trips.count { p in it.passengers } >= minTrips
//    }.toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
       allPassengers.filter {
           var countTrips = 0

           for(trip in trips) {
               if(trip.driver == driver && it in trip.passengers) {
                   countTrips++;
               }
           }
           countTrips >= 2
       }.toSet()

//allPassenger
//    .filter { p ->
//        trips.count { it.driver == driver && p in it.passengers } > 1
//    }
//    .toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
        allPassengers.filter {
            var drips :Int = 0; var ptrips:Int = 0

            for(trip in trips) {
                if(it in trip.passengers) {
                    if(ceil(trip.discount ?: 0.0).toInt() == 0) ptrips++
                    else drips++
                }
            }

            drips > ptrips
        }.toSet()

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    var tripMap = HashMap<Int,Int>()
    var freq = 0
    for(trip in trips) {
        val start = trip.duration/10
        freq = tripMap.get(start) ?: 0
        tripMap[start] = ++freq
    }
    var result : IntRange? = null
    var maxFreq = 0
    tripMap.forEach { start, freq ->
        if(freq > maxFreq) {
            maxFreq = freq
            result = (start*10)..(start*10+9)
        }
    }

    return result
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {

    var totalCost = 0.0

    var driverCost = HashMap<Driver, Double>()
    allDrivers.forEach { driverCost[it] = 0.0}

    trips.forEach { trip ->
        val cost = driverCost[trip.driver] ?: 0.0
        driverCost[trip.driver] = cost + trip.cost
        totalCost += trip.cost
    }

    val minDriver : Int = (allDrivers.size * 0.2).toInt()
    val costList = driverCost.map { it.value }.sortedByDescending { it }

    var dominantCost = 0.0
    for( i in 0 until minDriver) dominantCost += costList[i]

    return dominantCost / totalCost >= 0.8
}

