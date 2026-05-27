# Drinkshop — Android-клиент

Курсовая работа по дисциплине **«Разработка клиент-серверных мобильных приложений»**.
Клиентское Android-приложение магазина напитков с авторизацией Firebase,
[Ktor-бэкендом](https://github.com/itsbenzopila/drinkshop-backend-coutsework) и бонусной системой
для постоянных покупателей.

## Стек

| Слой          | Технология                                        |
| ------------- | ------------------------------------------------- |
| Язык          | Kotlin                                       |
| UI            | Jetpack Compose + Material 3                      |
| Архитектура   | Clean Architecture (data / domain / presentation) |
| DI            | Ручной ServiceLocator (`AppContainer`)            |
| Навигация     | androidx.navigation:navigation-compose            |      |
| Авторизация   | Firebase Auth (Email/Password)                    |

## Архитектура

```
app/src/main/java/com/itsbenzopila/drinkshop/
├── domain/                    
│   ├── model/                 
│   ├── repository/            
│   └── usecase/               
├── data/
│   ├── remote/
│   │   ├── api/               
│   │   ├── dto/               
│   │   ├── interceptor/       
│   │   └── NetworkModule.kt   
│   ├── mapper/                
│   └── repository/            
├── di/
│   └── AppContainer.kt        
└── presentation/
    ├── theme/                 
    ├── common/                
    ├── navigation/            
    ├── splash/                
    ├── auth/                  
    ├── catalog/               
    ├── cart/                  
    ├── orders/                
    └── profile/               
```

## Экраны

1. **Splash** — проверяет `FirebaseAuth.currentUser`. Если есть — синхронизируется с бэком и идёт в каталог, иначе — на Login.
2. **Login / Register** — Firebase Email/Password.
3. **Catalog** — категории сверху, сетка напитков с кнопкой «+» (добавить в корзину).
4. **Drink Detail** — карточка напитка с описанием и большой кнопкой «Добавить».
5. **Cart** — список товаров, кол-во, поле «списать баллы» (макс. 30 % от заказа), итог, кнопка «Оформить».
6. **Orders** — список заказов с начисленными/списанными баллами.
7. **Profile** — баланс баллов, профиль, выход.

## Бонусная система

- За каждые **100 ₽** заказа начисляется **10 баллов**.
- При оформлении можно списать **до 30 %** суммы заказа баллами (1 балл = 1 ₽).
- Логика считается на бэкенде — клиент только показывает баланс и передаёт желаемое к списанию.
