package org.rec.bitforge.knlaw;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.rec.bitforge.knlaw.dao.KeywordDao;
import org.rec.bitforge.knlaw.dao.PunishmentDao;
import org.rec.bitforge.knlaw.dao.RelatedLawDao;
import org.rec.bitforge.knlaw.entities.Law;
import org.rec.bitforge.knlaw.dao.LawDao;

@Database(entities = {Law.class}, version = 1)
public abstract class DB extends RoomDatabase {
    public abstract LawDao lawDao();
    public abstract KeywordDao keywordDao();
    public abstract RelatedLawDao relatedLawDao();

    public abstract PunishmentDao punishmentDao();
}
