<template>
    <div class="session-panel">
        <h1>
            Sessões
        </h1>
        <hr/>
        <LoadingPanel
            v-if="loading"
        />
        <ErrorPanel
            v-else-if="error"
            :message="error"
            :retry="load"
        />
        <div v-else-if="sessions.length === 0" class="message">
            Não há sessões disponíveis para este filme.
        </div>
        <div v-else class="content">
            <div class="row">
                <h3>
                    Unidade:
                </h3>
                <SelectButton
                    v-model="selection.location"
                    :options="locationOptions"
                    optionLabel="label"
                    optionValue="value"
                />
            </div>
            <div v-if="this.selection.location" class="row">
                <h3>
                    Dia:
                </h3>
                <SelectButton
                    v-model="selection.date"
                    :options="dateOptions"
                    optionLabel="label"
                    optionValue="value"
                />
            </div>
            <div v-if="this.selection.date" class="row">
                <h3>
                    Horário:
                </h3>
                <SelectButton
                    v-model="selection.time"
                    :options="timeOptions"
                    optionLabel="label"
                    optionValue="value"
                />
            </div>
            <InfoPanel
                v-if="session"
                title="Detalhes da sessão"
                :message="`Nesta sessão o filme será em ${twoOrThreeD} e ${dubbedOrSubtitled}.`"
            />
        </div>
    </div>
</template>

<script>
import SelectButton from 'primevue/selectbutton';
import LoadingPanel from '@/components/loading-panel/LoadingPanel.vue';
import ErrorPanel from '@/components/error-panel/ErrorPanel.vue';
import InfoPanel from '@/components/info-panel/InfoPanel.vue';
import SessionService from '@/services/session-service';

export default {
    name: 'SessionPanel',
    components: {
        SelectButton,
        LoadingPanel,
        ErrorPanel,
        InfoPanel
    },
    props: {
        movieId: {
            type: String,
            required: true
        }
    },
    data() {
        return {
            sessions: [],
            selection: {
                location: null,
                date: null,
                time: null
            },
            loading: false,
            error: null
        }
    },
    computed: {
        locationOptions() {
            const locations = [];

            for (let session of this.sessions) {
                const location = session.location;

                if (locations.find((other) => other.id === location.id)) {
                    continue;
                }

                locations.push(location);
            }

            const toOption = (location) => {
                return {
                    label: location.name,
                    value: location
                }
            };

            return locations.map(toOption);
        },
        dateOptions() {
            if (!this.selection.location) {
                return [];
            }

            const dates = [];

            for (let session of this.sessions) {
                const location = session.location;

                if (location.id !== this.selection.location.id) {
                    continue;
                }

                const date = session.date;

                if (dates.includes(date)) {
                    continue;
                }

                dates.push(date);
            }

            const toOption = (date) => {
                const parts = date.split('-');

                return {
                    label: `${parts[2]}/${parts[1]}`,
                    value: date
                }
            };

            return dates.map(toOption);
        },
        timeOptions() {
            if (!this.selection.location || !this.selection.date) {
                return [];
            }

            const times = [];

            for (let session of this.sessions) {
                const location = session.location;

                if (location.id !== this.selection.location.id) {
                    continue;
                }

                const date = session.date;

                if (date !== this.selection.date) {
                    continue;
                }

                const time = session.time;

                if (times.includes(time)) {
                    continue;
                }

                times.push(time);
            }

            const toOption = (time) => {
                const parts = time.split(':');

                return {
                    label: `${parts[0]}:${parts[1]}`,
                    value: time
                }
            };

            return times.map(toOption);
        },
        session() {
            const location = this.selection.location;

            if (!location) {
                return null;
            }

            const date = this.selection.date;

            if (!date) {
                return null;
            }

            const time = this.selection.time;

            if (!time) {
                return null;
            }

            for (let session of this.sessions) {
                if (session.location.id !== location.id) {
                    continue;
                }

                if (session.date !== date) {
                    continue;
                }

                if (session.time !== time) {
                    continue;
                }

                return session;
            }

            return null;
        },
        twoOrThreeD() {
            if (!this.session) {
                return null;
            }

            if (this.session.threeD) {
                return '3D';
            }

            return '2D';
        },
        dubbedOrSubtitled() {
            if (!this.session) {
                return null;
            }

            if (this.session.subtitled) {
                return 'legendado';
            }

            return 'dublado';
        }
    },
    watch: {
        'locationOptions'(newValue) {
            let location = null;

            if (newValue.length > 0) {
                location = newValue[0].value;
            }

            this.selection.location = location;
        },
        'selection.location'(newValue) {
            let date = null;

            if (newValue) {
                date = this.dateOptions[0].value;
            }

            this.selection.date = date;
        },
        'selection.date'(newValue) {
            let time = null;

            if (newValue) {
                time = this.timeOptions[0].value;
            }

            this.selection.time = time;
        }
    },
    methods: {
        async load() {
            this.sessions = [];
            this.selection.location = null;
            this.selection.date = null;
            this.selection.time = null;

            this.loading = true;
            this.error = null;

            try {
                this.sessions = await SessionService.getAll(this.movieId);
            } catch (error) {
                this.error = error.message;
            } finally {
                this.loading = false;
            }
        }
    },
    created() {
        this.load();
    }
}
</script>

<style lang="scss" scoped>
.content {
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.row {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

hr {
    margin-bottom: 30px;
}

h3 {
    margin: 0;
}
</style>