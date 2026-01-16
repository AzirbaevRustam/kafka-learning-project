Проект для изучения Kafka с микросервисами

Проект для практического изучения Apache Kafka с использованием Spring Boot микросервисов.

## Структура проекта

kafka-learning-project/
├── common-dto/               
│   ├── src/main/java/com/example/common/dto/
│   │   ├── UserEvent.java    
│   │   ├── OrderEvent.java    
│   │   └── OrderItem.java    
│   └── pom.xml
├── producer-service/         
│   ├── src/main/java/com/example/producer/
│   │   ├── controller/       
│   │   ├── service/         
│   │   └── dto/            
│   └── pom.xml
├── consumer-service/          
│   ├── src/main/java/com/example/consumer/
│   │   ├── entity/           
│   │   ├── repository/      
│   │   ├── service/         
│   │   └── config/          
│   └── pom.xml
├── docker/                    
│   └── docker-compose.yml    
├── pom.xml                  
└── README.md                

## Быстрый старт

### 1. Запуск инфраструктуры
\`\`\`bash
docker-compose up -d
\`\`\`

### 2. Создание топиков Kafka
\`\`\`bash
# Топик для событий пользователей
docker exec -it kafka kafka-topics.sh --create \
  --topic user-events \
  --bootstrap-server localhost:9092 \
  --partitions 1 \
  --replication-factor 1

# Топик для событий заказов
docker exec -it kafka kafka-topics.sh --create \
  --topic order-events \
  --bootstrap-server localhost:9092 \
  --partitions 1 \
  --replication-factor 1
\`\`\`

### 3. Сборка и запуск сервисов
\`\`\`bash
# Сборка общего модуля
cd common-dto && mvn clean install

# Запуск producer-service (порт 8081)
cd ../producer-service && mvn spring-boot:run

# В другом терминале запуск consumer-service (порт 8082)
cd ../consumer-service && mvn spring-boot:run
\`\`\`

## API Endpoints

### Producer Service (http://localhost:8081)
- \`POST /api/users\` - Создание события пользователя
- \`POST /api/orders\` - Создание события заказа

### Consumer Service (http://localhost:8082)
- Автоматически потребляет события из Kafka
- Сохраняет данные в PostgreSQL

## Тестирование

Создание пользователя:
\`\`\`bash
curl -X POST http://localhost:8081/api/users \
  -H \"Content-Type: application/json\" \
  -d '{
    \"username\": \"ivanov\",
    \"email\": \"ivan@example.com\",
    \"firstName\": \"Иван\",
    \"lastName\": \"Иванов\"
  }'
\`\`\`

Создание заказа:
\`\`\`bash
curl -X POST http://localhost:8081/api/orders \
  -H \"Content-Type: application/json\" \
  -d '{
    \"orderNumber\": \"ORD-001\",
    \"userId\": 1,
    \"items\": [
      {
        \"productId\": \"P001\",
        \"productName\": \"Ноутбук\",
        \"quantity\": 1,
        \"price\": 999.99
      }
    ]
  }'
\`\`\`

## Технологии
- Java 21
- Spring Boot 3.2.0
- Apache Kafka 3.7.0
- PostgreSQL 16
- Docker & Docker Compose
- Maven
- Spring Kafka
- Spring Data JPA
git commit -m "Добавлен README.md с документацией проекта и инструкциями по запуску"
