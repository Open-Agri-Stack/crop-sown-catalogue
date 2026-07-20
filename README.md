# Crop Sown Catalogue Service

A Spring Boot microservice that manages the catalogue data behind the **Crop Sown Registry** — the planning, cultivation, sowing and harvesting records captured for each farmer's plot — backed by PostgreSQL as the system of record, Elasticsearch for search, and Redis for caching.

## Overview

The Crop Sown Catalogue Service (`csr`) is part of the **OpenAgriStack** ecosystem. The Crop Sown Registry form has four tabs, and each tab's add-record popup maps 1:1 to a catalogue served by this service:

| Catalogue (entity) | Registry tab | Popup form |
|---|---|---|
| `plannedinput` | Planning | Planned Input |
| `actualinput` | Cultivation / Land Preparation | Actual Input |
| `sowingdetails` | Sowing | Sowing Details |
| `harvestingdetails` | Harvesting | Harvesting Details |

A fifth catalogue (the registry header / farmer identity) is planned.

## How a record flows

1. The request body is validated against the entity's JSON Schema in [`src/main/resources/payloadValidation/`](src/main/resources/payloadValidation/).
2. A primary id is generated from the schema's `prefix`/`keyLength` definition (e.g. `plannedinput-<12 chars>`).
3. The record is persisted to PostgreSQL (JPA, system of record).
4. The record is indexed into Elasticsearch — **only fields whose names appear as top-level keys in the entity's mapping file** in [`src/main/resources/EsFieldsmapping/`](src/main/resources/EsFieldsmapping/) are indexed, so the payload schema and ES mapping must keep exact key parity.
5. Search results are cached in Redis with a configurable TTL.

Field names in both JSON files are the registry form labels, verbatim. Repeating tables such as Water Resources, Pest Details and Pest / Disease Details are arrays of objects in the payload and `nested` types in the ES mapping.

## API

Every catalogue exposes the same five endpoints under `/<entity>/v1`:

| Method | Path | Description |
|---|---|---|
| POST | `/<entity>/v1/create` | Validate and create a record |
| POST | `/<entity>/v1/search` | Search (body: `SearchCriteria` — `pageNumber`, `pageSize`, `searchString`, `filterCriteriaMap`, …) |
| GET | `/<entity>/v1/read/{id}` | Read a record by its generated id |
| DELETE | `/<entity>/v1/delete/{id}` | Delete a record by id |
| POST | `/<entity>/v1/import` | Bulk import from a CSV/XLSX file (multipart `file`, max 5 MB; column headers must match schema property names exactly) |

With four catalogues that is 20 endpoints in total. A ready-to-use Postman collection with sample payloads for all of them is included at the repo root: [`crop-sown-catalogue.postman_collection.json`](crop-sown-catalogue.postman_collection.json) (collection variable `baseUrl` defaults to `http://localhost:8080`).

## Tech Stack

- **Java 17 / Spring Boot 3.3.5** (Maven, Lombok)
- **PostgreSQL** — system of record
- **Elasticsearch 8** — search index, per-entity field mappings
- **Redis** — search-result cache
- **Python 3.12** — `main.py` scaffolding CLI for new catalogues

## Getting Started

### Prerequisites

- Java 17 and Maven (or use the bundled `./mvnw`)
- Docker (for local infrastructure)

### Local infrastructure

```bash
docker compose up -d
```

This starts everything the service needs, with ports matching the defaults in `application.properties`:

| Service | Port | Notes |
|---|---|---|
| PostgreSQL 16 | 5433 | db `csr_db`, user `csr_user` |
| Redis 7 | 6379 | |
| Elasticsearch 8.13 | 9200 | user `elastic` |
| pgAdmin | 5050 | admin UI |
| Redis Commander | 8081 | admin UI |
| Kibana | 5601 | admin UI |

Connection settings can be overridden via environment variables (`SPRING_DATASOURCE_URL`, `SPRING_REDIS_HOST`, `ELASTICSEARCH_HOST`, …) — see `src/main/resources/application.properties`.

### Build & run

```bash
./mvnw clean install
./mvnw spring-boot:run
```

The service starts on port `8080`.

## Adding a new catalogue

Catalogues are scaffolded with the Python CLI (uses the templates in [`registry_template/`](registry_template/)):

```bash
python3 main.py --action create --name <entityname>
# e.g. the existing four:
python3 main.py --action create --name plannedinput,actualinput,sowingdetails,harvestingdetails
```

This generates the controller/entity/repository/service classes, stub `payloadValidation` and `EsFieldsmapping` JSONs (skipped if they already exist), and wires the entity into `Constants.java`, `VergProperties.java` and `application.properties`. `--action delete` reverses all of it.

**Naming rule:** use squashed lowercase entity names (`plannedinput`, not `planned-input` or `plannedInput`). Elasticsearch rejects uppercase index names, and the generated index is `<entityname>_index`.

After scaffolding, fill in the two JSON files with the real fields — keep their keys identical, since the ES mapping doubles as the indexing whitelist. `createEntities.sh` / `deleteEntities.sh` wrap the CLI for bulk runs (edit the entity list inside first).

## Project Structure

```
├── registry_template/                     # Java templates used by the scaffolder
├── main.py                                # Catalogue scaffolding CLI (create/delete)
├── crop-sown-catalogue.postman_collection.json
├── docker-compose.yml                     # Postgres, Redis, Elasticsearch + admin UIs
└── src/main/
    ├── java/com/catalogue/verg/
    │   ├── core/                          # Shared: cache, config, dto, elasticsearch, exception, logger, service, util
    │   ├── plannedinput/                  # Planning tab catalogue
    │   ├── actualinput/                   # Cultivation / Land Preparation tab catalogue
    │   ├── sowingdetails/                 # Sowing tab catalogue
    │   └── harvestingdetails/             # Harvesting tab catalogue
    │       └── controller|entity|repository|service/impl
    └── resources/
        ├── application.properties
        ├── payloadValidation/             # Draft-07 JSON Schemas (request validation + id generation)
        └── EsFieldsmapping/               # Per-entity ES field mappings (indexing whitelist)
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Open a pull request

## License

MIT — see [LICENSE](LICENSE).
