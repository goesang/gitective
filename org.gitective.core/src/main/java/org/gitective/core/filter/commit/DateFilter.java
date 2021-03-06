/*
 * Copyright (c) 2011 Kevin Sawicki <kevinsawicki@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package org.gitective.core.filter.commit;

import java.io.IOException;
import java.util.Date;

import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.gitective.core.Assert;

/**
 * Commit filter that includes commits until one is encountered that returns a
 * date from {@link #getDate(RevCommit)} that has occurred before the configured
 * date.
 */
public abstract class DateFilter extends CommitFilter {

	/**
	 * Time
	 */
	protected final long time;

	/**
	 * Create a date filter for a given time
	 *
	 * @param time
	 */
	public DateFilter(final long time) {
		this.time = time;
	}

	/**
	 * Create a date filter for a given date
	 *
	 * @param date
	 *            must be non-null
	 */
	public DateFilter(final Date date) {
		if (date == null)
			throw new IllegalArgumentException(Assert.formatNotNull("Date"));

		time = date.getTime();
	}

	@Override
	public boolean include(final RevWalk walker, final RevCommit commit)
			throws IOException {
		final Date date = getDate(commit);
		if (date == null)
			return include(false);
		if (date.getTime() < time)
			return include(false);
		return true;
	}

	/**
	 * Get the date from a given {@link PersonIdent}
	 *
	 * @param person
	 * @return date or null if given person is null
	 */
	protected Date getWhen(final PersonIdent person) {
		return person != null ? person.getWhen() : null;
	}

	/**
	 * Get a date from the commit to compare against
	 *
	 * @param commit
	 * @return date
	 */
	protected abstract Date getDate(RevCommit commit);
}
