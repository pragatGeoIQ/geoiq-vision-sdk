package com.geoiq.lk.test

import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Test suite that runs all unit tests for the GeoIQ-LK SDK.
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    GeoIQLKUnitTest::class,
    GeoIQLKConfigTest::class,
    GeoIQLKEventTrackTest::class,
    GeoIQLKApiSurfaceTest::class
)
class GeoIQLKTestSuite
