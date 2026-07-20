# BookMyStay

BookMyStay is a Spring Boot backend for a hotel booking and hotel management system.

It supports authentication, hotel administration, room management, inventory management, hotel browsing, booking, guest management, reviews, and a payment flow structure.

## Current Status

The backend is now in a runnable baseline state.

Implemented:

- User signup, login, JWT authentication, and refresh token support.
- Role-based access for users and hotel admins.
- Admin hotel CRUD.
- Admin room CRUD.
- Automatic inventory creation for rooms.
- Inventory viewing and update APIs.
- Hotel search by city, date range, room count, and pagination.
- Hotel detail API with available room prices.
- Booking initialization with inventory reservation.
- Add guests to a booking.
- Payment initiation with a local confirmation URL.
- Booking confirmation after payment confirmation.
- Booking cancellation with inventory release/update.
- User profile, guest list, and user bookings.
- Admin hotel booking list and revenue report.
- Reviews and ratings for users with confirmed bookings.
- Global exception handling.
- Swagger/OpenAPI dependency.

Verified:

```bash
.\mvnw.cmd test
```

Result:

```text
BUILD SUCCESS
Tests run: 7, Failures: 0, Errors: 0
```

## Tech Stack

| Technology | Usage |
| --- | --- |
| Java 21 | Core language |
| Spring Boot 4 | Backend framework |
| Spring Security | Authentication and authorization |
| JWT | Token authentication |
| Spring Data JPA | Database access |
| Hibernate | ORM |
| PostgreSQL | Database |
| Maven | Build tool |
| Lombok | Boilerplate reduction |
| ModelMapper | DTO mapping |
| Stripe Java SDK | Payment/webhook integration base |

## Project Structure

```text
src/main/java/com/BookMyStay/bookmystay
|-- Config
|-- Controller
|-- Dto
|-- Entity
|-- Exception
|-- Repository
|-- Security
|-- Service
`-- Util
```

## Configuration

Create a PostgreSQL database:

```sql
CREATE DATABASE bookmystaydb;
```

Configure `src/main/resources/application.properties`:

```properties
spring.application.name=bookmystay
server.port=8081
server.servlet.context-path=/api/v1

spring.datasource.url=jdbc:postgresql://localhost:5432/bookmystaydb?useSSL=false
spring.datasource.username=postgres
spring.datasource.password=your-password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secretKey=${JWT_SECRET_KEY:change-this-development-secret}

# Required only for real Stripe webhook verification
stripe.webhook.secret=${STRIPE_WEBHOOK_SECRET:}
```

Run:

```bash
.\mvnw.cmd spring-boot:run
```

Base URL:

```text
http://localhost:8081/api/v1
```

## Authentication APIs

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/auth/signup` | Create account |
| POST | `/auth/register` | Create account alias |
| POST | `/auth/login` | Login and receive access token |
| POST | `/auth/refresh` | Refresh access token from refresh cookie |

Signup accepts an optional `role` field:

```json
{
  "name": "Hotel Admin",
  "email": "admin@example.com",
  "password": "password123",
  "role": "ADMIN"
}
```

If `role` is not provided, the user is created as `USER`.

## User APIs

Requires authentication.

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/user/profile` | Get current profile |
| PATCH | `/user/profile` | Update profile |
| GET | `/user/guests` | Get saved guests |
| POST | `/user/guest` | Add guest |
| PUT | `/user/guest/{guestId}` | Update guest |
| DELETE | `/user/guest/{guestId}` | Delete guest |
| GET | `/user/myBookings` | Get current user's bookings |

## Admin Hotel APIs

Requires `ADMIN` role.

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/admin/hotel` | Add hotel |
| GET | `/admin/hotel` | Get hotels owned by admin |
| GET | `/admin/hotel/{hotelId}` | Get hotel by id |
| PUT | `/admin/hotel/{hotelId}` | Update hotel |
| DELETE | `/admin/hotel/{hotelId}` | Delete hotel |
| PATCH | `/admin/hotel/{hotelId}/active` | Activate hotel |
| GET | `/admin/hotel/{hotelId}/bookings` | Get hotel bookings |
| GET | `/admin/hotel/{hotelId}/reports` | Get booking revenue report |

## Admin Room APIs

Requires `ADMIN` role.

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/admin/hotel/{hotelId}/rooms` | Add room to hotel |
| GET | `/admin/hotel/{hotelId}/rooms` | Get hotel rooms |
| PUT | `/admin/hotel/{hotelId}/rooms/{roomId}` | Update room |
| DELETE | `/admin/hotel/{hotelId}/rooms/{roomId}` | Delete room |

Creating a room automatically initializes one year of inventory for that room.

## Inventory APIs

Requires `ADMIN` role and hotel ownership.

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/admin/inventory/rooms/{roomId}` | Get room inventory |
| PATCH | `/admin/inventory/rooms/{roomId}` | Update room inventory over a date range |

## Browse Hotel APIs

Public.

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/hotels/search` | Search hotels |
| GET | `/hotels/{hotelId}/info` | Get hotel info and available room prices |

Search example:

```text
GET /hotels/search?city=Delhi&startDate=2026-08-01&endDate=2026-08-03&roomsCount=1&page=0&size=10
```

Hotel info example:

```text
GET /hotels/1/info?startDate=2026-08-01&endDate=2026-08-03&roomsCount=1
```

## Booking APIs

Requires authentication.

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/bookings/init` | Initialize booking and reserve inventory |
| POST | `/bookings/{bookingId}/addGuests` | Attach saved guest ids |
| POST | `/bookings/{bookingId}/payments` | Initiate payment flow |
| POST | `/bookings/{bookingId}/payments/confirm?sessionId=...` | Confirm local payment |
| POST | `/bookings/{bookingId}/cancel` | Cancel booking |
| GET | `/bookings/{bookingId}/status` | Get booking status |

Booking statuses:

```text
RESERVED
GUESTS_ADDED
PAYMENTS_PENDING
CONFIRMED
CANCELLED
EXPIRED
```

## Review APIs

Review reads are public. Creating or updating a review requires authentication and a confirmed booking for that hotel.

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/hotels/{hotelId}/reviews` | Add or update review |
| GET | `/hotels/{hotelId}/reviews` | Get hotel reviews |

## Payment And Stripe Status

Payment is partially integrated.

What exists now:

- `stripe-java` dependency is present.
- `POST /bookings/{bookingId}/payments` creates a local payment session id and returns a local confirmation URL.
- `POST /bookings/{bookingId}/payments/confirm?sessionId=...` confirms the booking and moves inventory from reserved to booked.
- `POST /webhooks/stripe` exists and verifies Stripe webhook signatures if `stripe.webhook.secret` is configured.
- Stripe webhook event `checkout.session.completed` is wired to booking confirmation.

What is still not complete for real Stripe payments:

- The app does not yet create a real Stripe Checkout Session.
- `stripe.secret.key` is not currently used by the service.
- Success and cancel URLs are not configured.
- The booking payment session id is currently a local id, not a Stripe Checkout Session id.

So, adding only the Stripe API key is not enough yet.

To finish real Stripe payments, implement these remaining items:

- Add `stripe.secret.key=${STRIPE_SECRET_KEY}` to configuration.
- In `BookingServiceImpl.initiatePayments`, create a real Stripe Checkout Session using the booking amount.
- Store the Stripe Checkout Session id in `booking.paymentSessionId`.
- Return `session.getUrl()` instead of the local confirmation URL.
- Configure frontend/backend success and cancel URLs.
- Configure a Stripe webhook endpoint URL pointing to `/api/v1/webhooks/stripe`.
- Set `STRIPE_WEBHOOK_SECRET` from Stripe webhook settings.

Until those changes are added, the payment flow is suitable for local testing of booking confirmation, not production card payments.

## Recommended Workflow

```text
Admin signup -> Admin login -> Create hotel -> Create rooms -> Inventory auto-created
User signup -> User login -> Search hotels -> Add guests -> Init booking
Add guests to booking -> Initiate payment -> Confirm payment -> Booking confirmed
User with confirmed booking -> Add review
Admin -> View bookings and reports
```

## What Is Still Left

For a backend learning/project submission, the core hotel management and booking backend is now complete enough to run and demonstrate.

For production readiness, these are still left:

- Real Stripe Checkout Session creation.
- Email notifications.
- Image upload support for hotel and room photos.
- Stronger DTO validation on every request.
- Integration tests for booking, inventory, room, and review flows.
- Database migration tool such as Flyway or Liquibase.
- Docker setup.
- Admin dashboard/frontend.
- Better reporting and analytics.
- Booking expiry cleanup job for unpaid reservations.
