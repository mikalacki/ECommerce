# ECommerce Android App

### Author: Milos Mikalacki

#### Language: English

A mini e-commerce Android application that displays a list of products, product details, and allows
users to mark products as favorites.

## Features

- Product list (Paging 3)
- Product search
- Product details screen
- Favorite products (stored in Room database)
- White-label support (product flavors)

## Architecture

The app follows **MVVM + Clean Architecture principles**:

- UI: Fragments + ViewBinding
- ViewModel: state management
- Repository: single source of truth
- Data: Retrofit + Room

## Tech Stack

- Kotlin
- Hilt (Dependency Injection)
- Retrofit
- Room
- Paging 3
- Glide
- Material 3

## White-label Support

Implemented using product flavors:

- different app names
- different themes and colors
- different app icons
- support for different API endpoints

## Getting Started

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle
4. Select build variant:
    - `defaultClientDebug`
    - `partnerADebug`
    - `partnerBDebug`
5. Run the app

## Key Decisions

- MVVM for scalability and maintainability
- Hilt for dependency injection
- Paging 3 for efficient list loading
- Room for local data persistence
- Product flavors for white-label support

## Staged Rollout Strategy

Release strategy:

1. Internal testing
2. Gradual rollout (10% → 50% → 100%)
3. Monitoring (Crashlytics, performance)
4. Rollback in case of issues

## Planned Features

- Firebase Authentication (login/signup)
- Add to cart
- Checkout flow

---

# ECommerce Android App

### Autor: Miloš Mikalački

#### Jezik: Srpski

Mini e-commerce Android aplikacija koja prikazuje listu proizvoda, detalje i omogućava označavanje
proizvoda kao omiljenih.

## Funkcionalnosti

- Lista proizvoda (Paging 3)
- Pretrega proizvoda
- Detalji proizvoda
- Omiljeni proizvodi (Room baza)
- White-label podrška (product flavors)

## Arhitektura

Aplikacija koristi **MVVM + Clean Architecture principe**:

- UI: Fragments + ViewBinding
- ViewModel: state management
- Repository: single source of truth
- Data: Retrofit + Room

## Tehnologije

- Kotlin
- Hilt (DI)
- Retrofit
- Room
- Paging 3
- Glide
- Material 3

## White-label

Podržani su različiti klijenti putem product flavors:

- različit naziv aplikacije
- različite boje i tema
- različite ikonice
- mogućnost različitog API-ja

## Pokretanje

1. Clone repo
2. Open in Android Studio
3. Sync Gradle
4. Izaberi build variant:
    - `defaultClientDebug`
    - `partnerADebug`
    - `partnerBDebug`
5. Run app

## Ključne odluke

- MVVM zbog skalabilnosti i testabilnosti
- Hilt za dependency injection
- Paging 3 za efikasno učitavanje liste
- Room za lokalno čuvanje favorita
- Product flavors za white-label podršku

## Staged Rollout

Strategija izdavanja:

1. Interno testiranje
2. Postepeni rollout (10% → 50% → 100%)
3. Monitoring (Crashlytics, performanse)
4. Rollback u slučaju problema

## Planirani feature-i

- Firebase Authentication (login/signup)
- Add to cart
- Checkout flow

---
