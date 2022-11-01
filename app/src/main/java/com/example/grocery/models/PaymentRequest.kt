package com.example.grocery.models

import com.google.gson.annotations.SerializedName

data class PaymentRequest(
    val amount_cents: String,
    val auth_token: String,
    @SerializedName("items")
    val items: List<Item> = emptyList(),
    val currency: String="EGP",
    val delivery_needed: String = "false"
)
/*{
  "auth_token":  "ZXlKaGlPaUpJVXpVeE1pSX1Y0NJmV5Sn...",
  "delivery_needed": "false",
  "amount_cents": "100",
  "currency": "EGP",
  "merchant_order_id": 5,
  "items": [
    {
        "name": "ASC1515",
        "amount_cents": "500000",
        "description": "Smart Watch",
        "quantity": "1"
    },
    {
        "name": "ERT6565",
        "amount_cents": "200000",
        "description": "Power Bank",
        "quantity": "1"
    }
    ],
  "shipping_data": {
    "apartment": "803",
    "email": "claudette09@exa.com",
    "floor": "42",
    "first_name": "Clifford",
    "street": "Ethan Land",
    "building": "8028",
    "phone_number": "+86(8)9135210487",
    "postal_code": "01898",
     "extra_description": "8 Ram , 128 Giga",
    "city": "Jaskolskiburgh",
    "country": "CR",
    "last_name": "Nicolas",
    "state": "Utah"
  },
    "shipping_details": {
        "notes" : " test",
        "number_of_packages": 1,
        "weight" : 1,
        "weight_unit" : "Kilogram",
        "length" : 1,
        "width" :1,
        "height" :1,
        "contents" : "product of some sorts"
    }
}*/