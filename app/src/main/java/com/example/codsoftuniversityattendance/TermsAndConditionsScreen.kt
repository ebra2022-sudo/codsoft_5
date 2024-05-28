package com.example.codsoftuniversityattendance

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAAndConditionSScreen(navController: NavController) {
    Scaffold(topBar = { TopAppBar(
        title = { Text(text = "Terms and Conditions")},
        navigationIcon = { IconButton(onClick = { navController.navigate(Screens.LogInScreen.name)}) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }})}){
        BasicText(text = "Agreement\n" +
                "• The following terms and conditions shall apply to the use of Ethio telecom’s SuperApp and the services released. By using this application, you will be deemed to have read, understood, and accepted these Terms and Conditions.\n" +
                "\n" +
                "• Please carefully read this terms and conditions as it is important to inform you the policy, conditions, and responsibility to use the applications and the offers released on it.\n" +
                "\n" +
                "Purpose of the agreement\n" +
                "• The purpose of this agreement is to regulate the terms and conditions under which customers of Ethio telecom (telebirr) services to subscribe and use the app and services/offers available it.\n" +
                "\n" +
                "Ethio telecom SuperApp\n" +
                "• It is an application that enables customers to access Ethio telecom (telebirr) services including telebirr financial services (micro credit and saving), payments (merchant, Utility, ticket, fuel, bill, tax & government services, transport, entertainment,…), mini apps (educational, ticketing, government services, delivery services, entertainment, e-commerce …), cash-in and cash-out, money transfer, remittance, airtime recharge, package purchase, registration, statement, KYC information, My account …etc.\n" +
                "\n" +
                "Right and obligation of Ethio telecom:\n" +
                "• Ethio telecom will provide variety of telebirr services to satisfy customers need.\n" +
                "\n" +
                "• Ethio telecom shall have the right to devise, control, regulate and take all necessary measures on the use of the applications and services by the customers.\n" +
                "\n" +
                "• Ethio telecom reserves the right to modify, vary, amend the SuperApp, its features and these Terms and Conditions at any time. Such changes will be communicated through Ethio telecom’s website and its social media platforms.\n" +
                "\n" +
                "Right and obligation of customers\n" +
                "• Customers can download the App from Play Store or Appstore and Install the App on their handset or tablet.\n" +
                "\n" +
                "• Customer shall only use the application for the purpose given and allowed by Ethio telecom.\n" +
                "\n" +
                "• If customers use or abuse the SuperApp contrary to Ethio telecom’s terms and conditions or applicable legislation, Ethio telecom may prevent their continual usage of the SuperApp.\n" +
                "\n" +
                "• Customers must not share their password and account details with anyone else.\n" +
                "\n" +
                "• Customers must let us know by dialing 127, Sending SMS to 126 or sending inbox message to Ethio telecom social media platforms if they think there has been unauthorized use of their account and/or if their phone is stolen or missing.\n" +
                "\n" +
                "• Ethio telecom may require customers to input further information before they can access or launch SuperApp to confirm (authenticate) that they are the registered users of telebirr account.\n" +
                "\n" +
                "• Additional means of authentication may be required such as one-time password (OTP), biometrics detection, security questions or secret codes.\n" +
                "\n" +
                "• Customers will be requested to accept these terms and conditions and Ethio telecom’s privacy policy to enjoy access to several thrilling telebirr products and services on the SuperApp.\n" +
                "\n" +
                "Privacy:\n" +
                "• Ethio telecom respects customers data and their privacy.\n" +
                "\n" +
                "• However, Ethio telecom shall use customers data to provide and improve its products and services.\n" +
                "\n" +
                "Amendments\n" +
                "• Ethio telecom reserves the right to add to or amend these Terms and Conditions from time to time.\n" +
                "\n" +
                "• These Terms and Conditions are governed by Ethiopian law.\n" +
                "\n" +
                "• These Terms and Conditions are available on www.ethiotelecom.et.", modifier = Modifier
            .fillMaxSize()
            .padding(it).padding(16.dp))
    }
}