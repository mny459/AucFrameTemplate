package com.mny.wan.pkg.base

import com.mny.mojito.data.IRepository
import javax.inject.Inject

/**
 * Desc:
 */
abstract class BaseRepository {
    @Inject
    lateinit var mRepository: IRepository
}