# BloodBook
 An Android based application that provides a central platform for the donor, receiver and blood bank to assist in the management of blood supply chain for immediate communication during emergencies.

Initial Setup :

We have used Cloud Firestore for Database Management. Go through the documentation to get started with cloud firestore:

https://firebase.google.com/docs/firestore/quickstart

Next clone the project and build in Android Studio.

--------------------------------------------------------------------------------------------------

Functionalities provided by the application :

1) DONOR :

(a) To let the donor REGISTER first by providing details, and then LOGIN for further activities ( authentication purpose solved )

(b) Give a view of all the nearby Blood Banks after accepting the location from donor in the registration form

(c) Keeping the health factor at the topmost priority, letting the donor know his/her last donation date and history in accordance

(d) Functionality to show the available DONATION DRIVES in Pune so that donor is equipped with the address where to donate

(e) After Login showing the donor’s profile for more user friendliness and letting the system be more informative for the donor

2) RECEIVER :

(a) Accepting only the main details of the patient ( receiver ) so that distribution of the blood is easy and efficient

(b) Receiver should input the matching input for Blood Group for correct medicinal purposes and safety

(c) Capture Image of the person who would approach the Blood Bank for authentication that correct packet of blood from the blood bank is given to the correct person

(d) Facility to navigate the Blood Banks from the available list after retrieving the provided location by the receiver

(e) Application sends an auto generated email to the selected bloodbank with deatils to avail the packet when physically visited

3) BLOOD BANK :

(a) Keep a check on the available stock and display the stock of all blood groups included

(b) Considering fresh blood gets expired within 30 days ( 4 weeks ) notification to the blood bank that the blood is about to get expired ( i.e after 3 weeks ) and finally delete it after completion of 30 days ( SELECT AND DELETE MULTIPLE EXPIRED PACKETS )

(c) The time taken into consideration that when a receiver puts up a request for blood packets and when he physically receives the packet. Avoids duplication in number of units/packets available with the blood bank by displaying the pending requests to the blood bank

(d) To keep a track of how many packets have been delivered and requests processed since the use of application started by displaying history of requests

(e) Know how many donors are in contact with the application to notify when the stock of the blood bank reduces by time by displaying the list of donors registered at first

(f) As the health safety factor is given highest priority, verification of the donor whether 3 months are completed or not is done and accordingly access is enabled via app to donate blood again

--------------------------------------------------------------------------------------------------
