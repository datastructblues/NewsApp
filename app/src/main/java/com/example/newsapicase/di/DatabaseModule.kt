package com.example.newsapicase.di

import android.content.Context
import androidx.room.Room
import com.example.newsapicase.data.db.ArticleDAO
import com.example.newsapicase.data.db.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideUserDao(db: ArticleDatabase): ArticleDAO = db.getArticleDAO()

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        articleProvider: Provider<ArticleDAO>,
    ): ArticleDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ArticleDatabase::class.java,
            "article_db.db"
        ).build()
    }
}