package ru.job4j.wait;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Yury Matskevich
 * @since 0.1
 */
public class NotBlockingMemoryTest {

	@Test
	public void whenAddInformationThenMethodAddReturnTrueElseFalse() {
		NotBlockingMemory<String> memory = new NotBlockingMemory<>();
		assertTrue(memory.add(1, "First"));
		assertFalse(memory.add(1, "Second"));
	}

	@Test
	public void whenDeleteInformationFromMemoryMethodReturnTrueElseFalse() {
		NotBlockingMemory<String> memory = new NotBlockingMemory<>();
		memory.add(1, "First");
		assertTrue(memory.delete(1));
		assertFalse(memory.delete(1)); // записи с id = 1 уже была удалена
	}

	@Test
	public void whenUpdatedInformationThenMethodReturnTrueElseFalse() throws OptimisticException {
		NotBlockingMemory<Integer> memory = new NotBlockingMemory<>();
		memory.add(1, 10);
		assertTrue(memory.update(1, 3));
		assertFalse(memory.update(2, 10)); // записи с id = 2 нет.
	}

	private boolean flag = false; //было ли OptimisticException

	@Test
	public void whenFirstThreadUpdatedInformationAndSecondThradTryToUpdateInformationBeforeFirstOneDidNotEnd()
			throws InterruptedException {
		NotBlockingMemory<String> memory = new NotBlockingMemory<>();
		String s1 = "First";
		String s2 = "Second";
		memory.add(1, s1);

		Thread thread1 = new Thread() {
			@Override
			public void run() {
				try {
					memory.update(1, s2);
				} catch (OptimisticException e) {
					//
				}
			}
		};

		Thread thread2 = new Thread() {
			@Override
			public void run() {
				try {
					memory.update(1, s2);
				} catch (OptimisticException e) {
					flag = true;
				}
			}
		};
		/*thread2 после считывания записи с id = 1 ожидает 1000 мс,
		а в это время thread1 обновляет данные в записи
		c id = 1. После пробуждения thread2 продолжает работу(затирает
		данные, обнавленные thread1), что приводит к OptimisticException,
		а само хранилище возвращается к состоянию, в котором запись с id = 1
		модифицирована только thread1.
		 */
		thread2.setName("1000");
		thread1.setName("1");
		thread1.start();
		thread2.start();
		thread1.join();
		thread2.join();
		assertTrue(flag);
	}
}