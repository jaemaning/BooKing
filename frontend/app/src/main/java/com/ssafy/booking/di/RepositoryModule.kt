package com.ssafy.booking.di

import com.google.api.ResourceDescriptor.History
import com.ssafy.data.remote.api.BookSearchApi
import com.ssafy.data.remote.api.BookingApi
import com.ssafy.data.remote.api.BookingBoardApi
import com.ssafy.data.remote.api.ChatApi
import com.ssafy.data.remote.api.FirebaseApi
import com.ssafy.data.remote.api.KakaoPayApi
import com.ssafy.data.remote.api.HistoryAPi
import com.ssafy.data.remote.api.LocationApi
import com.ssafy.data.remote.api.MemberApi
import com.ssafy.data.remote.api.MyBookApi
import com.ssafy.data.remote.api.MyPageApi
import com.ssafy.data.remote.api.NaverCloudApi
import com.ssafy.data.repository.BookSearchRepositoryImpl
import com.ssafy.data.repository.BookingBoardRepositoryImpl
import com.ssafy.data.repository.BookingRepositoryImpl
import com.ssafy.data.repository.ChatRepositoryImpl
import com.ssafy.data.repository.FirebaseRepositoryImpl
import com.ssafy.data.repository.GoogleDataSourceImpl
import com.ssafy.data.repository.GoogleRepositoryImpl
import com.ssafy.data.repository.KakaoPayRepositoryImpl
import com.ssafy.data.repository.HistoryRepositoryImpl
import com.ssafy.data.repository.LocationRepositoryImpl
import com.ssafy.data.repository.MemberRepositoryImpl
import com.ssafy.data.repository.MyBookRepositoryImpl
import com.ssafy.data.repository.MyPageRepositoryImpl
import com.ssafy.data.repository.NaverCloudRepositoryImpl
import com.ssafy.domain.repository.BookSearchRepository
import com.ssafy.domain.repository.BookingBoardRepository
import com.ssafy.domain.repository.BookingRepository
import com.ssafy.domain.repository.ChatRepository
import com.ssafy.domain.repository.FirebaseRepository
import com.ssafy.domain.repository.GoogleRepository
import com.ssafy.domain.repository.KakaoPayRepository
import com.ssafy.domain.repository.HistoryRepository
import com.ssafy.domain.repository.LocationRepository
import com.ssafy.domain.repository.MemberRepository
import com.ssafy.domain.repository.MyBookRepository
import com.ssafy.domain.repository.MyPageRepository
import com.ssafy.domain.repository.NaverCloudRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMainRepository(
        googleDataSourceImpl: GoogleDataSourceImpl
    ): GoogleRepository {
        return GoogleRepositoryImpl(
            googleDataSourceImpl
        )
    }

    @Provides
    @Singleton
    fun provideMemberRepository(api: MemberApi): MemberRepository {
        return MemberRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideChatRepository(api: ChatApi): ChatRepository {
        return ChatRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideMyPageRepository(api: MyPageApi): MyPageRepository {
        return MyPageRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideBookSearchRepository(api: BookSearchApi): BookSearchRepository {
        return BookSearchRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideBookingRepository(api: BookingApi): BookingRepository {
        return BookingRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideFirebaseRepository(api: FirebaseApi): FirebaseRepository {
        return FirebaseRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideMyBookRepository(api: MyBookApi): MyBookRepository {
        return MyBookRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(api: LocationApi): LocationRepository {
        return LocationRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideNaverCloudRepository(api: NaverCloudApi) : NaverCloudRepository {
        return NaverCloudRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideKakaoPayRepository(api: KakaoPayApi) : KakaoPayRepository {
        return KakaoPayRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideBookingBoardRepository(api: BookingBoardApi) : BookingBoardRepository {
        return BookingBoardRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideHistoryRepository(api: HistoryAPi) : HistoryRepository {
        return HistoryRepositoryImpl(api)
    }
}
