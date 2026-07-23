# Naval Battle (Battleship) — Mini Project #4

**750014C Fundamentos de Programación Orientada a Eventos**
Universidad del Valle

Implementation of the classic strategy game "Battleship", played against a machine opponent, built with **JavaFX** following an **MVC** architecture, **SOLID** principles, and several **design patterns**.

---

## Team Members

| Name | ID |
|---|---|
| Luvian Steven Parra Rendón | 2518972 |
| Sara Michelle González Posso | 2519548 |
| Margareth Sophya Gamboa Izquierdo | 2518629 |
| Lissette Johana Patiño Quinayas | 2520977 |

---

## Description

Each player has a fleet of 10 ships (1 aircraft carrier, 2 submarines, 3 destroyers, 4 frigates) placed on a 10x10 board. The human player places their fleet manually (by dragging or clicking); the machine places its own fleet randomly. Players take turns firing at each other's fleet — a hit grants another shot, a miss ends the turn. The first player to sink the opponent's entire fleet wins.

Game progress is saved automatically after every move, and can be resumed exactly where it was left off.

---

## How to Run

Requirements: **JDK 17+** and **Maven**.

```bash
git clone https://github.com/OmegaZero216/miniproyecto-4-naval-battle-lepr-msgi-smgp-ljpq.git
cd miniproyecto-4-naval-battle-lepr-msgi-smgp-ljpq
mvn javafx:run
```

### Running the unit tests

```bash
mvn test
```

---

## Technologies

- **Language:** Java 17
- **GUI:** JavaFX 21 (FXML + Scene Builder)
- **Dependency management:** Maven
- **Testing:** JUnit 5 (Jupiter)
- **Version control:** Git and GitHub

---

## Project Structure

```
src/main/java/org/example/miniproyecto4navalbattleleprmsgismgpljpq/
├── Main.java                  # Entry point (JavaFX bootstrap)
├── config/                    # GameConfig (constants), ViewType (screen paths)
├── model/                     # Board, Cell, Coordinate, Ship, GameResult, GameSaveData, PlayerStats
│   ├── enums/                 # CellState, GamePhase, Orientation, ShipType
│   └── exception/             # Custom exceptions (see Exception Handling section)
├── service/                   # Game logic
│   ├── ai/                    # AIService + the machine's shooting strategies
│   ├── state/                 # State pattern (game phases)
│   ├── GameService.java       # Main orchestrator of a game session
│   ├── PlacementService.java  # Ship placement rule validation
│   ├── FleetFactory.java      # Generates the fleet placement order
│   └── RandomFleetPlacementService.java  # Random placement (machine's fleet)
├── repository/                 # Persistence: SaveManager, LoadManager, StatsRepository
├── controller/                 # JavaFX controllers (one per screen)
├── view/                       # 2D shape factories and visual utilities
└── event/                      # Custom event interfaces and adapters

src/main/resources/.../fxml/    # Views (.fxml) and stylesheet (.css)
src/test/java/                  # Unit tests (JUnit 5)
data/                            # Save files generated at runtime
```

---

## Screens

1. **Title** — nickname entry, start a new game or load a saved one.
2. **Preparation** — fleet placement (click or drag), with rotation (`R`) and confirmation (`Enter`).
3. **Game** — own board and opponent's board; real-time shot results, manual save button, and a debug mode (reveals the enemy fleet for 5 seconds, single use per game).
4. **Results** — summary of the finished game (shots fired, ships sunk) and the nickname's historical stats (accumulated wins/losses).

---

## Architecture

**MVC** pattern, with an additional **Service** layer between the View and the Model to keep Controllers free of game logic:

- **Model:** `Board`, `Ship`, `Cell`, `Coordinate` — pure data and rules that belong to the data itself.
- **View:** `.fxml` files plus shape factories (`CellShapeFactory`, `ShipPaletteItem`, `BoardHeaderFactory`).
- **Controller:** one Controller per screen, with no game logic — only receives input and delegates.
- **Service:** `GameService`, `PlacementService`, `AIService`, `StatsService` — where the actual game logic lives.

## Design Patterns

| Pattern | Category | Usage |
|---|---|---|
| Builder | Creational | `Ship.ShipBuilder` — step-by-step construction of an immutable ship |
| Factory Method | Creational | `FleetFactory`, `CellShapeFactory`, `BoardHeaderFactory`, `GameStateFactory` |
| State | Behavioral | `GameState` and its 4 phases (`PlacingShips`, `PlayerTurn`, `MachineTurn`, `GameOver`) |
| Strategy | Behavioral | `AIStrategy` (`RandomStrategy` / `TargetingStrategy`) for the machine's shooting |
| Observer | Behavioral | `GameStateListener`, notifies the UI of phase changes |
| Adapter | Structural | `CellClickAdapter` / `CellHoverAdapter`, translate raw JavaFX events into domain-specific interfaces |

## Persistence

- **Binary (serializable):** the full board state (`GameSaveData`) is saved with `ObjectOutputStream`/`ObjectInputStream` after every move.
- **Flat file (text):** per-nickname statistics (`data/stats/<nickname>.txt`) — wins, losses, total shots fired, total ships sunk.

## Exception Handling

- `InvalidShipPlacementException` (checked) — invalid ship placement.
- `GamePersistenceException` (checked) — failure while saving/loading.
- `SaveNotFoundException` (unchecked) — attempting to load with no saved game present.

## Unit Tests

5 test classes (JUnit 5) covering `Board`, `Ship`, `PlacementService`, `GameService`, and the save/load cycle (`PersistenceTest`).

```bash
mvn test
```

---

## License

Academic project developed for the course 750014C — Fundamentos de Programación Orientada a Eventos, Universidad del Valle.
