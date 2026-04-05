package org.rec.bitforge.knlaw;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.rec.bitforge.knlaw.dao.KeywordDao;
import org.rec.bitforge.knlaw.dao.PunishmentDao;
import org.rec.bitforge.knlaw.dao.RelatedLawDao;
import org.rec.bitforge.knlaw.entities.Keyword;
import org.rec.bitforge.knlaw.entities.Law;
import org.rec.bitforge.knlaw.dao.LawDao;
import org.rec.bitforge.knlaw.entities.Punishment;
import org.rec.bitforge.knlaw.entities.RelatedLaw;

@Database(entities = {Keyword.class, Law.class, RelatedLaw.class, Punishment.class}, version = 1)
public abstract class DB extends RoomDatabase {
    public abstract LawDao lawDao();
    public abstract KeywordDao keywordDao();
    public abstract RelatedLawDao relatedLawDao();

    public abstract PunishmentDao punishmentDao();
}
