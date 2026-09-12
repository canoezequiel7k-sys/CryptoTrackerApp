com.cursokotlin.crypto_tracker_app/
│
├── core/                         <-- Módulos globales e inyección
│   ├── di/                       <-- Módulos de Hilt (NetworkModule, DatabaseModule)
│   └── util/                     <-- Clases de utilidad (Result/Resource, Extensiones)
│
├── crypto/                       <-- Feature principal de Criptomonedas
│   ├── data/                     <-- Implementación de repositorios, Retrofit API, Room DAO/Entities, Mappers
│   │   ├── local/
│   │   ├── remote/
│   │   ├── mapper/
│   │   └── repository/
│   ├── domain/                   <-- Modelos de dominio puros, Interfaces de Repositorio, Use Cases
│   │   ├── model/
│   │   ├── repository/
│   │   └── usecase/
│   └── presentation/             <-- UI con Compose (Pantallas, Composables, ViewModels, States)
│       ├── coin_list/
│       └── coin_detail/
│
└── ui/theme/                     <-- Colores, Tipografías y Temas de Compose (Material 3)