/*
 *
 * Copyright (C) 2007 The kune development team (see CREDITS for details)
 * This file is part of kune.
 *
 * Kune is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kune is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.ourproject.kune.platf.server.users;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.ourproject.kune.platf.server.domain.User;
import org.ourproject.kune.platf.server.manager.impl.DefaultManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class UserManagerDefault extends DefaultManager<User, Long> implements UserManager {
    private final User finder;

    @Inject
    public UserManagerDefault(final Provider<EntityManager> provider, final User finder) {
        super(provider, User.class);
        this.finder = finder;
    }

    public List<User> getAll() {
        return finder.getAll();
    }

    public User getByShortName(final String shortName) {
        return finder.getByShortName(shortName);
    }

    public User login(final String nickOrEmail, final String passwd) {
        // TODO: integrate a existing Auth manager
        User user;
        try {
            user = finder.getByShortName(nickOrEmail);
        } catch (NoResultException e) {
            try {
                user = finder.getByEmail(nickOrEmail);
            } catch (NoResultException e2) {
                return null;
            }
        }
        if (user.getPassword().equals(passwd)) {
            return user;
        } else {
            return null;
        }
    }

    public User createUser(final String shortName, final String longName, final String email, final String passwd) {
        User user = new User(shortName, longName, email, passwd);
        return user;
    }

    public User find(final Long userId) {
        return userId != null ? super.find(userId) : User.UNKNOWN_USER;
    }

    public List<User> search(final String search) {
        return this.search(search, null, null);
    }

    public List<User> search(final String search, final Integer firstResult, final Integer maxResults) {
        MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[] { "name", "shortName" },
                new StandardAnalyzer());
        Query query;
        try {
            query = parser.parse(search);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing search");
        }
        return super.search(query, firstResult, maxResults);
    }

}
