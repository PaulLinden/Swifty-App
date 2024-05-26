# Swifty Android App 

## Table of Contents

- [Description](#description)
- [Setup/Configuration](#setupconfiguration)


## Description

This Android application is developed as part of my thesis, aiming to enhance the efficiency and accessibility of retail. Built using Java and adhering to the MVVM (Model-View-ViewModel) architecture, the app provides a streamlined platform for retail operations.

It's important to note that this project is currently a work in progress, serving as a minimum viable product. Over
time, it will undergo enhancements to bolster its robustness and scalability.

## Setup/Configuration

To make the app runnable you need to connect i to the serverApplication. In project-root create a local.properties file and add the following:

BASE_URL=http://{YOUR_IP}:3000/api/
LOGIN=users/loginUser
COMPANY=firebase/data?path=companies
TRANSACTION=transaction

Replace YOUR_IP with the your ip-address.

Congratulation now you are all set. Just don't forget to make sure that the server application is running first, have fun.