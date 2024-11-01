<template>
    <div class="home-view">
        <h1>
            Filmes em cartaz
        </h1>
        <LoadingPanel
            v-if="loading"
        />
        <span v-else-if="error" class="error">
            {{ error }}
        </span>
        <span v-else-if="movies.length == 0" class="message">
            Não há filmes em cartaz no momento.
        </span>
        <MovieGrid
            v-else
            :movies="movies"
        />
    </div>
</template>

<script>
import LoadingPanel from '@/components/loading-panel/LoadingPanel.vue';
import MovieGrid from '@/components/movie-grid/MovieGrid.vue';
import MovieService from '@/services/movie-service.js';

export default {
    name: 'HomeView',
    components: {
        LoadingPanel,
        MovieGrid
    },
    data() {
        return {
            movies: [],
            loading: false,
            error: null
        }
    },
    methods: {
        async loadMovies() {
            this.loading = true;
            this.error = null;

            try {
                this.movies = await MovieService.getAll(true);
            } catch (error) {
                this.error = error.message;
            } finally {
                this.loading = false;
            }
        }
    },
    mounted() {
        this.loadMovies();
    }
}
</script>

<style lang="scss" scoped>
.home-view {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 30px;
}

h1 {
    font-size: 50px;
}

@media (max-width: 1280px) {
    h1 {
        font-size: 35px;
    }
}

.error {
    color: #ff0000;
}
</style>