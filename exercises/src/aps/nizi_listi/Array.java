package aps.nizi_listi;

public class Array<E> {
	private E data[]; // declared to be an Object since it would be too
						// complicated with generics
	private int size;

	public Array(int size) {
		data = (E[]) new Object[size];
		this.size = size;
	}

	public void set(int position, E o) {
		if (position >= 0 && position < size)
			data[position] = o;
		else
			System.out.println("Ne moze da se vmetne element na dadenata pozicija");
	}

	public E get(int position) {
		if (position >= 0 && position < size)
			return data[position];
		else 
			System.out.println("Ne e validna dadenata pozicija");
		return null;
	}

	public int getLength() {
		return size;
	}
	
	public int find(E o) {
		for (int i = 0; i < size; i++){
			if(o.equals(data[i]))
				return i;
		}
		return -1;
	}

	public void insert(int position, E o) {
		// before all we check if position is within range
		if (position >= 0 && position <= size) {
			// first resize the storage array
			E[] newData = (E[]) new Object[size + 1];
			// copy the data prior to the insertion
			for (int i = 0; i < position; i++)
				newData[i] = data[i];
			// insert the new element
			newData[position] = o;
			// move the data after the insertion
			for (int i = position; i < size; i++)
				newData[i + 1] = data[i];
			// replace the storage with the new array
			data = newData;
			size = size + 1;
		}
	}

	public void delete(int position) {
		// before all we check if position is within range
		if (position >= 0 && position < size) {
			// first resize the storage array
			E[] newData = (E[]) new Object[size - 1];
			// copy the data prior to the delition
			for (int i = 0; i < position; i++)
				newData[i] = data[i];
			// move the data after the deletion
			for (int i = position + 1; i < size; i++)
				newData[i - 1] = data[i];
			// replace the storage with the new array
			data = newData;
			size = size - 1;
		}
	}

	public void resize(int newSize) {
		// first resize the storage array
		E[] newData = (E[]) new Object[newSize];
		// copy the data
		int copySize = size;
		if (newSize < size)
			copySize = newSize;
		for (int i = 0; i < copySize; i++)
			newData[i] = data[i];
		// replace the storage with the new array
		data = newData;
		size = newSize;
	}

}
