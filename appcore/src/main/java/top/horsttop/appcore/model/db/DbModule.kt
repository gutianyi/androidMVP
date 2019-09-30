package top.horsttop.appcore.model.db

import android.app.Application
import android.arch.persistence.db.SupportSQLiteOpenHelper
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory

import com.squareup.sqlbrite3.BriteDatabase
import com.squareup.sqlbrite3.SqlBrite

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import top.horsttop.appcore.BuildConfig

/**
 * Created by horsttop on 2018/4/16.
 */

@Module
class DbModule {
    @Provides
    @Singleton
    internal fun provideSqlBrite(): SqlBrite {
        return SqlBrite.Builder()
                .logger { message -> Timber.tag("Database").v(message) }
                .build()
    }

    @Provides
    @Singleton
    internal fun provideDatabase(sqlBrite: SqlBrite, application: Application): BriteDatabase {
        val configuration = SupportSQLiteOpenHelper.Configuration.builder(application)
                .name(BuildConfig.DB_NAME)
                .callback(DbCallback())
                .build()
        val factory = FrameworkSQLiteOpenHelperFactory()
        val helper = factory.create(configuration)
        val db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io())
        db.setLoggingEnabled(true)
        return db
    }
}