package de.atp.data;

import android.util.SparseArray;

/**
 * Status of a row
 */
public enum RowStatus {

	/**
	 * Answer of user is okay
	 */
	OK(0),
	/**
	 * Question of user was aborted
	 */
	ABORTED(-1),
	/**
	 * User hasn't answered the questions yet
	 */
	DIRTY(-2);

	/**
	 * Number in the target file format
	 */
	private final int status;

	private RowStatus(final int status) {
		this.status = status;
	}

	/**
	 * @return Number fot eh target file format
	 */
	public int getStatus() {
		return status;
	}

	// Quick access of the enums by status int
	private static SparseArray<RowStatus> mapByStatus;

	static {
		// Instead of hash maps, Android Java suggests me to use SparseArray, so
		// what
		mapByStatus = new SparseArray<RowStatus>();

		for (RowStatus status : RowStatus.values()) {
			mapByStatus.put(status.status, status);
		}
	}

	/**
	 * 
	 * @param status
	 *            Number from the file
	 * @return Row status
	 */
	public static RowStatus getStatus(int status) {
		return mapByStatus.get(status);
	}

}
