<template>
    <div class="movie-grid">
        <MovieItem
            v-for="(movie, index) in movies"
            :key="index"
            :movie="movie"
        />
    </div>
</template>

<script>
import MovieItem from './MovieGridMovieItem.vue';

export default {
    name: 'MovieGrid',
    components: {
        MovieItem
    },
    props: {
        movies: {
            type: Array,
            required: true
        }
    }
}
</script>

<style lang="scss" scoped>
.movie-grid {
    display: flex;
    flex-direction: row;
    gap: 30px;
    flex-wrap: wrap;
    justify-content: center;
}
</style>