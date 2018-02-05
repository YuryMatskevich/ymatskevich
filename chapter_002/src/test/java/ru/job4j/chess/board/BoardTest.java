package ru.job4j.chess.board;

import org.junit.Assert;
import org.junit.Test;
import ru.job4j.chess.exeption.*;
import ru.job4j.chess.figure.Elephant;
import ru.job4j.chess.figure.Figure;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Yury Matskevich
 * @since 0.1
 */
public class BoardTest {

    @Test
    public void whenAddFigureThenItExists() throws ImpossibleMoveException, OccupiedWayException {
        Board board = new Board();

        board.add(new Elephant(new Cell("c2")));
        try {
            /**
             * Если фигура добавилась, то повторное добавление фигуры в эту же
             * клетку приведет к исключению OccupiedWayException
             */
            board.add(new Elephant(new Cell("c2")));
            Assert.fail("в try не было OccupiedWayException");
        } catch (OccupiedWayException e) {
            Assert.assertNotEquals("", e);
        }
    }

    @Test
    public void whenAddFigureInIllegalCellThenException() throws ImpossibleMoveException, OccupiedWayException {
        Board board = new Board();

        try {
            /**
             * шахматное поле [a, h] [1, 8] или [1, 8] [1, 8]
             */
            board.add(new Elephant(new Cell("i1")));
            Assert.fail("в try не было ImpossibleMoveException");
        } catch (ImpossibleMoveException e) {
            Assert.assertNotEquals("", e);
        }
    }

    @Test
    public void whenMoveFigureThenItIsInOtherCell() throws ImpossibleMoveException, OccupiedWayException, FigureNotFoundException {
        Board board = new Board();

        board.add(new Elephant(new Cell("c1")));
        /**
         * слон делает ход с1-f4 (теперь с1 свободна)
         */
        board.move(new Cell("c1"), new Cell("f4"));
        /**
         * с1 свободна, можем добавить новую фигуру в этой ячейки
         */
        board.add(new Elephant(new Cell("c1")));

        try {
            /**
             * ячейка f4 занята
             */
            board.add(new Elephant(new Cell("f4")));
            Assert.fail("в try не было OccupiedWayException");
        } catch (OccupiedWayException e) {
            Assert.assertNotEquals("", e);
        }
    }

    @Test
    public void whenMoveFigureThroughOtherFigureThenException() throws ImpossibleMoveException, OccupiedWayException, FigureNotFoundException {
        Board board = new Board();
        board.add(new Elephant(new Cell("c1")));
        board.add(new Elephant(new Cell("e3")));

        try {
            board.move(new Cell("c1"), new Cell("g5"));
            Assert.fail("в try не было OccupiedWayException");
        } catch (OccupiedWayException e) {
            Assert.assertNotEquals("", e);
        }
    }

    @Test
    public void whenMoveFigureThenGetWay() throws ImpossibleMoveException {
        Figure figure = new Elephant(new Cell("f8"));

        Cell[] way = figure.way(new Cell("f8"), new Cell("a3"));
        Cell[] ways = {new Cell("e7"), new Cell("d6"), new Cell("c5"), new Cell("b4"), new Cell("a3")};

        for (int i = 0; i < way.length; i++) {
            assertThat(way[i].cellEquals(ways[i]), is(true));
        }
    }

}
