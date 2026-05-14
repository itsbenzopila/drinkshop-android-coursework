# Drinkshop — Android-клиент

Курсовая работа по дисциплине **«Разработка клиент-серверных мобильных приложений»**.
Клиентское Android-приложение магазина напитков с авторизацией Firebase, REST-обменом с
[нашим Ktor-бэкендом](https://github.com/itsbenzopila/drinkshop-backend) и бонусной системой
для постоянных покупателей.

## Стек

| Слой          | Технология                                        |
| ------------- | ------------------------------------------------- |
| Язык          | Kotlin 2.0                                        |
| UI            | Jetpack Compose + Material 3                      |
| Архитектура   | Clean Architecture (data / domain / presentation) |
| DI            | Ручной ServiceLocator (`AppContainer`)            |
| Навигация     | androidx.navigation:navigation-compose            |
| Сеть          | Retrofit 2 + OkHttp + kotlinx.serialization       |
| Авторизация   | Firebase Auth (Email/Password)                    |
| Картинки      | Coil                                              |
| Min / Target  | minSdk 24, targetSdk 35                           |

## Архитектура

```
app/src/main/java/com/itsbenzopila/drinkshop/
├── domain/                    # чистый Kotlin
│   ├── model/                 # User, Drink, Cart, Order, ...
│   ├── repository/            # интерфейсы репозиториев
│   └── usecase/               # SignIn/SignUp/AddToCart/PlaceOrder/...
├── data/
│   ├── remote/
│   │   ├── api/               # Retrofit-интерфейсы
│   │   ├── dto/               # serializable DTO
│   │   ├── interceptor/       # AuthInterceptor (Firebase ID-token)
│   │   └── NetworkModule.kt   # Retrofit / OkHttp / Json
│   ├── mapper/                # DTO -> domain
│   └── repository/            # реализации
├── di/
│   └── AppContainer.kt        # ручной DI
└── presentation/
    ├── theme/                 # Material 3 тема
    ├── common/                # UiState
    ├── navigation/            # AppNavGraph, Screen
    ├── splash/                # Splash
    ├── auth/                  # Login, Register
    ├── catalog/               # Catalog, Drink Detail
    ├── cart/                  # Cart, оформление заказа
    ├── orders/                # история заказов
    └── profile/               # профиль + бонусные баллы + Sign out
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

## Запуск

### 1. Файлы Firebase

В `app/` уже лежит `google-services.json` (проект Firebase `android-drinkshop`).
Если ты используешь свой проект Firebase — замени этот файл.

### 2. Бэкенд

Подними локально бэкенд из [drinkshop-backend](https://github.com/itsbenzopila/drinkshop-backend):

```bash
cd ../drinkshop-backend
docker compose up -d
./gradlew run
```

Сервер слушает `http://localhost:8080`. С эмулятора Android этот хост виден как
`http://10.0.2.2:8080` — это и есть `API_BASE_URL` по умолчанию.

### 3. Запуск приложения

Открой проект в Android Studio (Hedgehog или новее) → Sync → Run на эмуляторе.

#### Своя машина / реальное устройство

Если бэкенд крутится не на эмуляторе, а на другом хосте — переопредели base URL
через `local.properties`:

```
drinkshop.apiBaseUrl=http://192.168.0.10:8080/
```

> **Важно**: для cleartext-HTTP уже разрешены `10.0.2.2` и `localhost` в
> `res/xml/network_security_config.xml`. Если будешь подключаться по другому
> хостнейму — добавь его туда.

## Тестовый сценарий

1. Запускаешь приложение → попадаешь на Login.
2. **Регистрация** → Email + пароль (≥ 6 символов) + имя → создаётся аккаунт в Firebase, бэк сразу синхронизирует пользователя.
3. **Каталог** → выбираешь напиток → добавляешь в корзину.
4. **Корзина** → меняешь количество, списываешь баллы (если есть) → «Оформить заказ».
5. **Заказы** → видишь свой заказ + начисленные баллы.
6. **Профиль** → проверяешь баланс баллов → «Выйти».
