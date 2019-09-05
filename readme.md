<p align="center"><img src="pics/logo.png" alt="Travelmantics" height="200px"></p>

# Travelmantics
[![](https://img.shields.io/badge/Reviewed_by-Hound-a873d1.svg)](https://houndci.com) [![Maintainability](https://api.codeclimate.com/v1/badges/2f9958e786faf2ff6e91/maintainability)](https://codeclimate.com/github/MamboBryan/Travelmantics/maintainability) [![Test Coverage](https://api.codeclimate.com/v1/badges/2f9958e786faf2ff6e91/test_coverage)](https://codeclimate.com/github/MamboBryan/Travelmantics/test_coverage) [![Build Status](https://app.bitrise.io/app/f09995fea2d5cbb3/status.svg?token=2Z_w0hDxM95mf7zGurlntQ)](https://app.bitrise.io/app/f09995fea2d5cbb3)

Travelmantics is a simple app that helps users find the best travel deals within their location.

This app uses firebase as its backend and can do the following:
- Users can login/signup and view all the current deals and share with their friends.
- Admins can login/signup with admin privileges, they can see, add, update and delete new travel deals.

I have used the following components for development:
- FirebaseUI
- Constraint Layout
- RecyclerView

###### For app demo [click here](https://appetize.io/app/d0a5dbrjgxdhb5hy8t1tzt0qum?device=nexus5&scale=75&orientation=portrait&osVersion=8.1)
###### For final app apk [click here](https://drive.google.com/open?id=1jn339O3c6l9jyo1NCQSA0bQDib0sorDu) to download

## Prerequisites
You will need the following to run this project:
1. A machine with internet access (Laptop or desktop)
2. Android studio 3.4 or later

## Set up
* Clone the project repository
* Open the project folder using Android Studio IDE
* Wait to for build to finish and you're good to go

## App Usage
#### Login/Signup
Users can login/signup to the app with their google account or email.

<img src="pics/login_activity.png" width="200"/>	<img src="pics/login_with_gmail.png" width="200"/> <img src="pics/login_with_email.png" width="200"/> <img src="pics/signup_with_email.png" width="200"/>

#### Travel Deals List
Users can view a list of travel deals while for admins have the option of adding new deals.

<img src="pics/deals_user.png" width="200"/> <img src="pics/deals_admin.png" width="200"/>

#### Travel Deal
Users can view a specific travel deal and share it by mail while an admin can edit the travel deal.

<img src="pics/deal_user.png" width="200"/> <img src="pics/deal_admin.png" width="200"/>

#### Updating travel deals
An admin can create and save a new travel deal or update and delete an existing travel deal.

<img src="pics/admin_privileges.png" width="200"/>

## Contributions
Find any typos? Have another resource you think should be included? Contributions are welcome!
* First, fork this repository
* Next, clone this repository to your desktop/laptop to make changes by

		` $ git clone {YOUR_REPOSITORY_CLONE_URL}`
		` $ cd Travelmantics`

* Once you've pushed changes to your local repository, you can issue a pull request by clicking on the green pull request icon.
