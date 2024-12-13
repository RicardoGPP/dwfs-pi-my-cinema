<template>
    <div class="movie-panel">
        <div class="poster">
            <img
                :src="movie.posterPath"
                :alt="movie.title"
                :title="movie.title"
            />
        </div>
        <div class="info">
            <div class="row">
                <h1 class="title">
                    {{ movie.title }}
                </h1>
                <h2 class="tagline">
                    {{ `"${movie.tagline}"` }}
                </h2>
            </div>
            <div class="row">
                <h3>
                    Lançamento:
                </h3>
                <span>
                    {{ formattedReleaseDate }}
                </span>
            </div>
            <div class="row">
                <h3>
                    Duração:
                </h3>
                <span>
                    {{ movie.runtime }} minutos
                </span>
            </div>
            <div class="row">
                <h3>
                    Gênero(s):
                </h3>
                <span>
                    {{ oneRowGenres }}
                </span>
            </div>
            <div class="row">
                <h3>
                    Sinopse:
                </h3>
                <span>
                    {{ movie.overview }}
                </span>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'MoviePanel',
    props: {
        movie: {
            type: Object,
            required: true
        }
    },
    computed: {
        formattedReleaseDate() {
            const parts = this.movie.releaseDate.split('-');

            const day = parts[2];
            const month = parts[1];
            const year = parts[0];

            return `${day}/${month}/${year}`;
        },
        oneRowGenres() {
            let oneRowGenres = '';

            for (let genre of this.movie.genres) {
                if (oneRowGenres !== '') {
                    oneRowGenres += ', ';
                }
                oneRowGenres += genre;
            }

            return oneRowGenres;
        }
    }
}
</script>

<style lang="scss" scoped>
.movie-panel {
    display: flex;
    flex-direction: row;
    justify-content: center;
    flex-wrap: wrap;
    gap: 20px;
    z-index: 2;

    & * {
        z-index: 2;
    }
}

.poster {
    width: 300px;
    height: 450px;
    border-radius: 5px;
    overflow: hidden;

    & > img {
        width: 100%;
        height: 100%;
    }
}

.info {
    display: flex;
    flex-direction: column;
    width: calc(100% - 320px);
    gap: 35px;
}

.row {
    display: flex;
    flex-direction: column;
    gap: 5px;

    &:first-child {
        gap: 25px;
    }
}

h1 {
    font-size: 45px;
}

h2 {
    font-size: 25px;
    color: #00FF7F;
}

h3 {
    font-size: 20px;
}

h1, h2, h3 {
    margin: 0px;
}

span {
    font-size: 18px;
}

@media (max-width: 1024px) {
    .poster {
        margin-top: 30px;
    }

    .info {
        width: 100%;
        margin-top: 120px;
    }

    .row:first-child {
        min-height: auto;
    }

    h1, h2 {
        text-align: center;
    }
}
</style>