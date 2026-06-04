# Vehicle Registration & Licensing System

Spring Boot application matching the project plan: vehicle registration with admin approval, annual licenses, inspections (6‑month validity for renewal), automatic late‑renewal fines, and fine payment.

## Oracle Database (HR schema)

1. Connect as a user with the **HR** schema (sample database user `HR`).
2. Ensure `HR` can create objects (or pre‑create tables). Hibernate `ddl-auto=update` creates **`VRL_*`** tables so they do not collide with Oracle’s sample **`EMPLOYEES`** / **`DEPARTMENTS`** tables.
3. Edit `src/main/resources/application.properties`:
   - `spring.datasource.url` — your JDBC URL (e.g. `jdbc:oracle:thin:@//localhost:1521/FREEPDB1` or `ORCLPDB1`).
   - `spring.datasource.username=HR`
   - `spring.datasource.password=` — your HR password.
4. `spring.jpa.properties.hibernate.default_schema=HR` keeps new entities under the HR schema when the JDBC user is HR.

Optional tuning:

- `vrl.fine.amount` — late renewal fine (default `50`).
- `vrl.inspection.upcoming-days` — window for “upcoming” inspections (default `30`).

## Run

```bash
./mvnw spring-boot:run
```

On first startup, if `VRL_APP_USER` is empty, seed users are created: **`admin`** (ADMIN) and **`jdoe`** (OWNER). Use `GET /users/1` and `/users/2` after startup to confirm IDs in your environment.

## API (JSON)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/vehicles/register` | Register vehicle (`PENDING`). Body: `ownerUserId`, `plateNumber`, `make`, `model`, `modelYear`. |
| GET | `/vehicles/{id}` | Vehicle details. |
| GET | `/vehicles/owner/{ownerId}` | Vehicles for an owner. |
| PUT | `/vehicles/{id}/approve` | Approve registration. Header: **`X-Admin-User-Id`** (must be `ADMIN`). Issues first 1‑year license. |
| PUT | `/vehicles/{id}/reject` | Reject registration. Header: **`X-Admin-User-Id`**. |
| POST | `/licenses/renew/{vehicleId}` | Renew license: requires latest **PASS** inspection within **6 months**; if current license is expired, creates an unpaid **fine** then extends expiry by **1 year** from today. |
| GET | `/licenses/{vehicleId}` | Current license (latest by expiry) for that vehicle id. |
| GET | `/licenses/expired` | Latest license per vehicle where that license is expired. |
| POST | `/inspections/{vehicleId}` | Add inspection. Body: `inspectionDate` (optional, default today), `result` (`PASS` / `FAIL`). Sets **next due** to inspection date **+ 6 months**. |
| GET | `/inspections/upcoming` | Inspection rows whose **next due** date is within the configured day window. |
| GET | `/fines/{ownerId}` | All fines for an owner. |
| POST | `/fines/pay/{fineId}` | Mark fine paid. |
| GET | `/users/{id}` | Lookup seeded or created users (roles). |

### Example bodies

**Register vehicle**

```json
{
  "ownerUserId": 2,
  "plateNumber": "ABC-123",
  "make": "Toyota",
  "model": "Corolla",
  "modelYear": 2022
}
```

**Add inspection**

```json
{
  "inspectionDate": "2026-05-01",
  "result": "PASS"
}
```

## Tests

Tests use **H2 in memory** (`src/test/resources/application.properties`) so `mvn test` does not require Oracle.

## JDK note

The project is configured for **Java 8** and **Spring Boot 2.7.18** so it builds with older JDKs. If you move to **JDK 17+**, you can upgrade to Spring Boot 3.x and switch `jakarta.*` imports back to `jakarta.*`.
