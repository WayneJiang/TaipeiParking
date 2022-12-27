package com.wayne.taipeiparking.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.wayne.taipeiparking.AppViewModel
import com.wayne.taipeiparking.R
import java.time.ZoneId
import java.util.*

class ProfilePreferenceFragment : PreferenceFragmentCompat() {
    companion object {
        var updateTimezone = 0
    }

    private val mAppViewModel: AppViewModel by lazy {
        ViewModelProvider(this)[AppViewModel::class.java]
    }

    private val mNameProfileItemPreference by lazy {
        findPreference<Preference>("key_name")
    }

    private val mPhoneProfileItemPreference by lazy {
        findPreference<Preference>("key_phone")
    }

    private val mEmailProfileItemPreference by lazy {
        findPreference<Preference>("key_email")
    }

    private val mTimezoneProfileItemPreference by lazy {
        findPreference<ListPreference>("key_timezone")
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference_profile)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timeZoneMap = mutableMapOf<String, Int>()
        TimeZone.getAvailableIDs().map { id ->
            if (TimeZone.getTimeZone(ZoneId.of(id)).displayName.contains("GMT")) {
                timeZoneMap[TimeZone.getTimeZone(ZoneId.of(id)).displayName] =
                    TimeZone.getTimeZone(ZoneId.of(id)).rawOffset / 3600000
            }
        }

        mAppViewModel.queryUser().observe(viewLifecycleOwner) {
            it?.apply {
                mNameProfileItemPreference?.summary = name
                mPhoneProfileItemPreference?.summary = phone
                mEmailProfileItemPreference?.summary = email
                mTimezoneProfileItemPreference?.apply {
                    summary =
                        timeZoneMap.filterValues { value -> value == timezone }.keys.elementAt(0)

                    setValueIndex(entries.indexOf(summary))

                    updateTimezone = timezone
                }
            }
        }

        mTimezoneProfileItemPreference?.apply {
            entries = timeZoneMap.keys.toTypedArray()
            entryValues =
                timeZoneMap.values.map { it.toString() }.toTypedArray()

            setOnPreferenceChangeListener { _, newValue ->

                val zone = (newValue as String).toInt()
                summary = entries[timeZoneMap.values.indexOfFirst { it == zone }]
                setDefaultValue(entryValues[timeZoneMap.values.indexOfFirst { it == zone }])

                updateTimezone = zone

                return@setOnPreferenceChangeListener true
            }
        }
    }
}