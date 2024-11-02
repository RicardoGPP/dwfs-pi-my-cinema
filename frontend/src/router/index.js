import { createRouter, createWebHistory } from 'vue-router';
import { jwtDecode } from 'jwt-decode';
import HomeView from '@/views/HomeView.vue';
import MoviesView from '@/views/MoviesView.vue';
import MovieDetailView from '@/views/MovieDetailView.vue';
import LocationsView from '@/views/LocationsView.vue';
import SessionsView from '@/views/SessionsView.vue';
import ProfileView from '@/views/ProfileView.vue';
import UnauthorizedView from '@/views/UnauthorizedView.vue';
import NotFoundView from '@/views/NotFoundView.vue';

const routes = [
    {
        path: '/',
        name: 'home',
        component: HomeView
    },
    {
        path: '/movies',
        name: 'movies',
        component: MoviesView,
        meta: {
            requiresAdmin: true
        }
    },
    {
        path: '/movies/:id',
        name: 'movie-detail',
        component: MovieDetailView,
        props: true
    },
    {
        path: '/locations',
        name: 'locations',
        component: LocationsView,
        meta: {
            requiresAdmin: true
        }
    },
    {
        path: '/sessions',
        name: 'sessions',
        component: SessionsView,
        meta: {
            requiresAdmin: true
        }
    },
    {
        path: '/profile',
        name: 'profile',
        component: ProfileView
    },
    {
        path: '/unauthorized',
        name: 'unauthorized',
        component: UnauthorizedView
    },
    {
        path: '/:pathMatch(.*)*',
        name: 'not-found',
        component: NotFoundView
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

router.beforeEach((to, _, next) => {
    const token = localStorage.getItem('token');

    let admin = false;

    if (token) {
        admin = jwtDecode(token).admin;
    }

    if (to.meta.requiresAdmin && !admin) {
        return next({ name: 'unauthorized' });
    }

    next();
});

export default router;