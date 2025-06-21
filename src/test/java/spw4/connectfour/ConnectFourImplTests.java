package spw4.connectfour;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConnectFourImplTests {

    @Nested
    @DisplayName("ctor tests initializes components correctly")
    class ctorTests {
        @DisplayName("players are set correctly")
        @ParameterizedTest
        @EnumSource(Player.class)
        void ctor_SetsPlayersCorrectly(Player playerOnTurn) {
            ConnectFour connectFour = new ConnectFourImpl(playerOnTurn);
            assertEquals(playerOnTurn, connectFour.getPlayerOnTurn());
        }
    }

    @Nested
    @DisplayName("getPlayerOnTurn returns the correct player")
    class getPlayerOnTurnTests {
        @DisplayName("players are returned correctly")
        @ParameterizedTest
        @EnumSource(Player.class)
        void getPlayerOnTurn_ReturnsPlayersCorrectly(Player playerOnTurn) {
            ConnectFour connectFour = new ConnectFourImpl(playerOnTurn);
            assertEquals(playerOnTurn, connectFour.getPlayerOnTurn());
        }

        @DisplayName("Player changes after each legal drop")
        @Test
        void getPlayerOnTurn_ChangePlayersAfterEachLegalDrop() {
            ConnectFour connectFour = new ConnectFourImpl(Player.red);
            Player playerBeforeDrop = connectFour.getPlayerOnTurn();
            connectFour.drop(3);
            Player playerAfterDrop = connectFour.getPlayerOnTurn();
            assertNotEquals(playerBeforeDrop, playerAfterDrop);
        }

        @DisplayName("Player changes back after each second legal drop")
        @Test
        void getPlayerOnTurn_ChangePlayersBackAfterEachSecondLegalDrop() {
            ConnectFour connectFour = new ConnectFourImpl(Player.red);
            Player playerBeforeDrop = connectFour.getPlayerOnTurn();
            connectFour.drop(3);
            connectFour.drop(3);
            Player playerAfterDrop = connectFour.getPlayerOnTurn();
            assertEquals(playerBeforeDrop, playerAfterDrop);
        }
    }

    @Nested
    @DisplayName("toString returns correct length board and player ...")
    class ToStringTests {
        @DisplayName("red")
        @Test
        void toString_ReturnsStringWithCertainLengthAndContainPlayerRed() {
            ConnectFour connectFour = new ConnectFourImpl(Player.red);
            String result = connectFour.toString();
            assertAll(
                    () -> assertEquals(156, result.length()),
                    () -> assertTrue(result.contains("RED")),
                    () -> assertTrue(result.contains("|"))
            );
        }

        @DisplayName("yellow")
        @Test
        void toString_ReturnsStringWithCertainLengthAndContainPlayerYellow() {
            ConnectFour connectFour = new ConnectFourImpl(Player.yellow);
            String result = connectFour.toString();
            assertAll(
                    () -> assertEquals(159, result.length()),
                    () -> assertTrue(result.contains("YELLOW"))
            );
        }

        @DisplayName("none")
        @Test
        void toString_ReturnsStringWithCertainLengthAndContainPlayerNone() {
            ConnectFour connectFour = new ConnectFourImpl(Player.none);
            String result = connectFour.toString();
            assertAll(
                    () -> assertEquals(157, result.length()),
                    () -> assertTrue(result.contains("NONE"))
            );
        }

    }

    @Nested
    @DisplayName("getPlayerAt returns correct player on the specified tile")
    class GetPlayerAtTests {
        @DisplayName("Players on empty board")
        @ParameterizedTest
        @CsvSource({
                "3, 0",
                "5, 4",
                "4, 4",
                "0, 4"
        })
        void getPlayerAt_ReturnsCorrectPlayerOnTheSpecifiedTile(int row, int col) {
            ConnectFour connectFour = new ConnectFourImpl(Player.red);
            assertEquals(Player.none, connectFour.getPlayerAt(row, col));
        }

        @DisplayName("Players on played Board")
        @Test
        void getPlayerAt_ReturnsCorrectPlayerOnTheSpecifiedTile_PlayedBoard() {
            ConnectFour connectFour = new ConnectFourImpl(Player.red);
            connectFour.drop(3);
            assertEquals(Player.red, connectFour.getPlayerAt(5, 3));
        }

    }

    @Nested
    @DisplayName("drop sets marker in the correct column")
    class DropMarkerTests {
        @DisplayName("Test the bottom row")
        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6})
        void drop_SetsMarkerInTheCorrectColumn(int col) {
            ConnectFour connectFour = new ConnectFourImpl(Player.red);
            connectFour.drop(col);
            assertEquals(Player.red, connectFour.getPlayerAt(5, col));
        }

        @DisplayName("Test multiple drops")
        @ParameterizedTest
        @CsvSource({
                "3, 0",
                "6, 4",
                "4, 4",
                "0, 4"
        })
        void drop_SetsMarkerInTheCorrectColumnAndSetsNewPlayer(int col1, int col2) {
            ConnectFour connectFour = new ConnectFourImpl(Player.red);
            connectFour.drop(col1);
            connectFour.drop(col2);
            assertAll(
                    () -> assertEquals(Player.red, connectFour.getPlayerAt(5, col1)),
                    () -> assertEquals(Player.yellow, connectFour.getPlayerAt(col1 == col2 ? 4 : 5, col2))
            );
        }

        @DisplayName("Test full column the game does not change player if the player tries to access a full column")
        @Test
        void drop_SetsMarkerInTheCorrectColumnAndSetsNewPlayerFull() {
            ConnectFour connectFour = new ConnectFourImpl(Player.red);
            for (int i = 0; i < 6; i++) {
                connectFour.drop(3);
            }
            Player playerBeforeFullDrop = connectFour.getPlayerOnTurn();
            connectFour.drop(3);
            Player playerAfterFullDrop = connectFour.getPlayerOnTurn();
            assertEquals(playerBeforeFullDrop, playerAfterFullDrop);
        }
    }

    @Nested
    @DisplayName("reset sets board to the starting position")
    class ResetBoardTests {
        @DisplayName("all tiles are blank after reset")
        @ParameterizedTest
        @EnumSource(Player.class)
        void reset_SetsBoardToStartingPosition(Player player) {
            ConnectFour connectFour = new ConnectFourImpl(Player.red);
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    connectFour.drop(j);
                }
            }
            connectFour.reset(player);
            assertAll(
                    () -> assertTrue(!connectFour.toString().contains(" Y ") && !connectFour.toString().contains(" R ")),
                    () -> assertEquals(player, connectFour.getPlayerOnTurn())
            );
        }
    }

    @Nested
    @DisplayName("isGameOver returns if the game is finished")
    class IsGameOverTests {
        @DisplayName("Horizontal win returns true")
        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void isGameOver_HorizontalWin_ReturnsTrue(int startingCol) {
            ConnectFour connectFour = new ConnectFourImpl(Player.red);
            for (int i = startingCol; i < startingCol + 4; i++) {
                connectFour.drop(i);
                connectFour.drop(i);
            }
            assertTrue(connectFour.isGameOver());
        }

        @DisplayName("Vertical win returns true")
        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6})
        void isGameOver_VerticalWin_ReturnsTrue(int startingCol) {
            ConnectFour connectFour = new ConnectFourImpl(Player.red);
            for (int i = 0; i < 4; i++) {
                connectFour.drop(startingCol);
                if (i < 3) {
                    connectFour.drop((startingCol + 1) % 7);
                }
            }
            assertTrue(connectFour.isGameOver());
        }

        @DisplayName("Positive diagonal (/) win returns true")
        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void isGameOver_PositiveDiagonalWin_ReturnsTrue(int startingCol) {
            ConnectFour connectFour = new ConnectFourImpl(Player.red);

            connectFour.drop(startingCol);

            connectFour.drop(startingCol + 1);
            connectFour.drop(startingCol + 1);

            connectFour.drop(startingCol);
            connectFour.drop( startingCol + 2);
            connectFour.drop( startingCol + 2);
            connectFour.drop( startingCol + 2);

            connectFour.drop(startingCol + 3);
            connectFour.drop(startingCol + 3);
            connectFour.drop(startingCol + 3);
            connectFour.drop(startingCol + 3);

            assertTrue(connectFour.isGameOver());
        }


        @DisplayName("Negative diagonal (\\) win returns true")
        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void isGameOver_NegativeDiagonalWin_ReturnsTrue(int startingCol) {
            ConnectFour connectFour = new ConnectFourImpl(Player.red);

            connectFour.drop(startingCol + 3);

            connectFour.drop(startingCol + 2);
            connectFour.drop(startingCol + 2);

            connectFour.drop(startingCol);
            connectFour.drop(startingCol);
            connectFour.drop(startingCol);
            connectFour.drop(startingCol);

            connectFour.drop(startingCol + 2);

            connectFour.drop(startingCol + 1);
            connectFour.drop(startingCol + 1);
            connectFour.drop(startingCol + 1);

            assertTrue(connectFour.isGameOver());
        }

        @DisplayName("No win on board return false")
        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void isGameOver_NoWinOnBoard_ReturnFalse(int drops) {
            ConnectFour connectFour = new ConnectFourImpl(Player.red);
            for (int i = 0; i < drops * 2; i++) {
                connectFour.drop(i % 2 == 0 ? 4 : 3);
            }
            assertFalse(connectFour.isGameOver());
        }
    }

    @Nested
    @DisplayName("getWinner returns the victor of the game or if no one has won no player")
    class GetWinnerTests {
        @DisplayName("returns winner after win")
        @ParameterizedTest
        @MethodSource("validPlayers")
        void getWinner_WithWinOnBoard_ReturnsWinner(Player player) {
            ConnectFour connectFour = new ConnectFourImpl(player);
            for (int i = 0; i < 4; i++) {
                connectFour.drop(i);
                if (i < 3) {
                    connectFour.drop(i);
                }
            }
            assertEquals(player, connectFour.getWinner());
        }

        static Stream<Arguments> validPlayers() {
            return Stream.of(Player.values())
                    .filter(p -> p != Player.none)
                    .map(Arguments::of);
        }

        @DisplayName("returns no winner before win")
        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void getWinner_NoWinOnBoard_ReturnsNoWinner(int drops) {
            ConnectFour connectFour = new ConnectFourImpl(Player.red);
            for (int i = 0; i < drops; i++) {
                connectFour.drop(i);
            }
            assertEquals(Player.none, connectFour.getWinner());
        }
    }
}
