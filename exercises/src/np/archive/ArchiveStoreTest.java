package np.archive;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class NonExistingItemException extends RuntimeException {
    NonExistingItemException(String message) {
        super(message);
    }
}

abstract class Archive {
    private int id;
    private Date dateArchived;

    Archive(int id) {
        this.id = id;
        dateArchived = new Date();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateArchived() {
        return dateArchived;
    }

    public void setDateArchived(Date dateArchived) {
        this.dateArchived = dateArchived;
    }

    abstract boolean canBeOpened(Date when);
}


class LockedArchive extends Archive {

    Date dateToOpen;

    LockedArchive(int id, Date dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    @Override
    boolean canBeOpened(Date when) {
        return dateToOpen.before(when);
    }
}


class SpecialArchive extends Archive {

    int maxOpen;
    private int timesOpened;

    SpecialArchive(int id, int maxOpen) {
        super(id);
        timesOpened = 0;
        this.maxOpen = maxOpen;
    }

    @Override
    boolean canBeOpened(Date when) {
        if (timesOpened < maxOpen) {
            timesOpened++;
            return true;
        }
        return false;

    }
}

class ArchiveStore {

    List<Archive> archives;
    List<String> log;

    ArchiveStore() {
        archives = new LinkedList<>();
        log = new LinkedList<>();
    }

    void archiveItem(Archive item, Date date) {
        item.setDateArchived(date);
        archives.add(item);
        log.add(String.format("Item %d archived at %s", item.getId(), item.getDateArchived()));
    }

    void openItem(int id, Date date) {
        for (Archive archive : archives) {
            if (archive.getId() == id) { // item exists

                if (archive instanceof LockedArchive) {
                    if (archive.canBeOpened(date))
                        log.add(String.format("Item %d opened at %s", id, date.toString()));
                    else {
                        LockedArchive lockedArchive = (LockedArchive) archive;
                        log.add(String.format("Item %d cannot be opened before %s", id, lockedArchive.dateToOpen));
                    }
                }
                if (archive instanceof SpecialArchive) {
                    SpecialArchive specialArchive = (SpecialArchive) archive;
                    if (archive.canBeOpened(date))
                        log.add(String.format("Item %d opened at %s", id, date.toString()));
                    else
                        log.add(String.format("Item %d cannot be opened more than %d times", id, specialArchive.maxOpen));
                }
                return;
            }
        }
        throw new NonExistingItemException("Item with id " + id + " doesn't exist");
    }

    String getLog() {
        return String.join("\n", log).replaceAll("GMT", "UTC");
    }

}


public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        Date date = new Date(113, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();
            Date dateToOpen = new Date(date.getTime() + (days * 24 * 60
                    * 60 * 1000));
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while (scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch (NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}
