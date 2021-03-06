/**
 * Copyright (C) 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.dashboard.provider;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jboss.dashboard.LocaleManager;
import org.jboss.dashboard.commons.filter.AbstractFilter;
import org.jboss.dashboard.dataset.DataSet;

public class DefaultDataSetFilter extends AbstractFilter implements DataFilter {

    /**
     * Decimal format for comparison in filter.
     */
    private static DecimalFormat _numberComparisonFormat = new DecimalFormat("00000000000000000000000000000000000000000000000000.0000000000");

    /**
     * Date format for comparison in filter.
     */
    public static final SimpleDateFormat _dateComparisonFormat = new SimpleDateFormat("yyyyMMddHHmm");

    protected DataSet dataSet;

    public DefaultDataSetFilter(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    protected String formatForDisplay(String propertyId, Object value) {
        DataPropertyFormatter formatter = DataFormatterRegistry.lookup().getPropertyFormatter(propertyId);
        return formatter.formatValue(propertyId, value, LocaleManager.currentLocale());
    }

    protected String formatForComparison(String propertyId, Object value) {
        // Some types need to be prepared for comparison.
        if (value instanceof Number) return _numberComparisonFormat.format(value);
        if (value instanceof Date) return _dateComparisonFormat.format((Date) value);

        // Format by default.
        return formatForDisplay(propertyId, value);
    }

    protected Object getPropertyValue(String propertyId, Object obj) {
        try {
            Object[] instance = (Object[]) obj;
            DataProperty prop = dataSet.getPropertyById(propertyId);
            int column = dataSet.getPropertyColumn(prop);
            return instance[column];
        } catch (ClassCastException e) {
            return null;
        }
    }
}
