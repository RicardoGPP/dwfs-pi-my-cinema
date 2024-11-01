<template>
    <div class="movie-detail-view">
        <LoadingPanel
            v-if="loading"
        />
        <span v-else-if="error" class="error">
            {{ error }}
        </span>
        <div v-else class="container">
            <div class="backdrop">
                <img :src="path"/>
            </div>
            <div class="content">
                <MovieDetail
                    :movie="movie"
                />
                <CommentPanel
                    :movieId="id"
                />
            </div>
        </div>
    </div>
</template>

<script>
import LoadingPanel from '@/components/loading-panel/LoadingPanel.vue';
import MovieDetail from '@/components/movie-detail/MovieDetail.vue';
import CommentPanel from '@/components/comment-panel/CommentPanel.vue';
import MovieService from '@/services/movie-service';

export default {
    name: 'MovieDetailView',
    components: {
        LoadingPanel,
        MovieDetail,
        CommentPanel
    },
    props: {
        id: {
            type: String,
            required: true
        }
    },
    data() {
        return {
            movie: null,
            loading: false,
            error: null
        }
    },
    computed: {
        path() {
            if (!this.movie) {
                return null;
            }

            const backdropPath = this.movie.backdropPath;

            if (!backdropPath) {
                return null;
            }

            return `https://image.tmdb.org/t/p/w780/${backdropPath}`;
        }
    },
    methods: {
        async loadMovie() {
            this.loading = true;
            this.error = null;

            try {
                this.movie = await MovieService.getById(this.id);
            } catch (error) {
                this.error = error.message;
            } finally {
                this.loading = false;
            }
        }
    },
    created() {
        this.loadMovie();
    }
}
</script>

<style lang="scss" scoped>
.movie-detail-view {
    display: flex;
    justify-content: center;
}

.container {
    position: relative;
    display: flex;
    justify-content: center;
    width: 100%;
}

.backdrop {
    position: absolute;
    width: 100%;
    height: 300px;
    overflow: hidden;

    & img {
        display: block;
        width: 100%;
        height: 100%;
        object-fit: cover;
        opacity: 0.2;
    }

    &::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        height: 50%;
        background: linear-gradient(to bottom, #00000000 0%, #00000099 100%);
        z-index: 1;
    }
}

.content {
    position: absolute;
    display: flex;
    flex-direction: column;
    gap: 30px;
    width: 1024px;
    padding: 20px;
    margin-top: 80px;
}

.error {
    color: #ff0000;
}

@media (max-width: 1024px) {
    .backdrop {
        height: 680px;
    }

    .content {
        width: 100%;
    }
}
</style>