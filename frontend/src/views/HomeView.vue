<template>
    <div class="home-view">
        <h1>
            Filmes em cartaz
        </h1>
        <LifecyclePanel :callback="load">
            <span v-if="movies.length == 0" class="message">
                Não há filmes em cartaz no momento.
            </span>
            <MovieGrid
                v-else
                :movies="movies"
            />
        </LifecyclePanel>
    </div>
</template>

<script>
import LifecyclePanel from '@/components/lifecycle-panel/LifecyclePanel.vue';
import MovieGrid from '@/components/movie-grid/MovieGrid.vue';
import MovieService from '@/services/movie-service.js';

export default {
    name: 'HomeView',
    components: {
        LifecyclePanel,
        MovieGrid
    },
    data() {
        return {
            movies: []
        }
    },
    methods: {
        async load() {
            this.movies = await MovieService.getAll(true);
        }
    }
}
</script>

<style lang="scss" scoped>
.home-view {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 30px;
    padding: 20px;
}

.error {
    color: #ff0000;
}

h1 {
    font-size: 50px;
}

@media (max-width: 1280px) {
    h1 {
        font-size: 35px;
    }
}
</style>