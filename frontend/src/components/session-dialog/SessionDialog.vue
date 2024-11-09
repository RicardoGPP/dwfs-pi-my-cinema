<template>
    <div class="session-dialog">
        <Dialog
            v-if="visible"
            v-model:visible="visible"
            :header="`${action} de sessão`"
            :draggable="false"
            modal
            contentStyle="width: 400px">

            <!-- Movie -->
            <div class="input">
                <label for="movie">
                    *Filme:
                </label>
                <Select
                    id="movie"
                    v-model="session.target.movie"
                    :options="movieOptions"
                    optionLabel="label"
                    optionValue="value"
                    filter
                    emptyMessage="Não há filmes disponíveis"
                />
            </div>

            <!-- Location -->
            <div class="input">
                <label for="location">
                    *Localização:
                </label>
                <Select
                    id="location"
                    v-model="session.target.location"
                    :options="locationOptions"
                    optionLabel="label"
                    optionValue="value"
                    filter
                    emptyMessage="Não há localizações disponíveis"
                />
            </div>

            <!-- Date -->
            <div class="input">
                <label for="date">
                    *Data:
                </label>
                <DatePicker
                    id="date"
                    v-model="date"
                    dateFormat="dd/mm/yy"
                    showIcon
                    fluid
                    iconDisplay="input"
                    placeholder="Digite a data"
                />
            </div>

            <!-- Time -->
            <div class="input">
                <label for="time">
                    *Hora:
                </label>
                <DatePicker
                    id="time"
                    v-model="time"
                    placeholder="Digite a hora"
                    showIcon
                    fluid
                    iconDisplay="input"
                    timeOnly
                />
            </div>

            <!-- Additional details -->
            <div class="input">
                <label>
                    *Detalhes adicionais:
                </label>
                <div class="flex items-center gap-5">
                    <Checkbox
                        id="threeD"
                        v-model="session.target.threeD"
                        binary
                    />
                    <label for="threeD">
                        3D
                    </label>
                </div>
                <div class="flex items-center gap-5">
                    <Checkbox
                        id="subtitled"
                        v-model="session.target.subtitled"
                        binary
                    />
                    <label for="subtitled">
                        Legendado
                    </label>
                </div>
            </div>

            <!-- Actions -->
            <template #footer>
                <Button
                    label="Aplicar"
                    :disabled="!canApply"
                    @click="apply"
                />
                <Button
                    label="Cancelar"
                    class="p-button-secondary"
                    @click="close"
                />
            </template>
        </Dialog>
    </div>
</template>

<script>
import Select from 'primevue/select';
import DatePicker from 'primevue/datepicker';
import Checkbox from 'primevue/checkbox';
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';
import MovieService from '@/services/movie-service';
import LocationService from '@/services/location-service';
import _ from 'lodash';

export default {
    name: 'SessionDialog',
    components: {
        Select,
        DatePicker,
        Checkbox,
        Dialog,
        Button
    },
    data() {
        return {
            visible: false,
            callback: null,
            mode: null,
            movies: [],
            locations: [],
            session: {
                source: null,
                target: null
            }
        }
    },
    computed: {
        action() {
            if (this.mode === 'create') {
                return 'Criação';
            }
            return 'Edição';
        },
        movieOptions() {
            return this.movies.map(this.toOption);
        },
        locationOptions() {
            return this.locations.map(this.toOption);
        },
        date: {
            get() {
                const date = this.session.target.date;

                if (!date) {
                    return null;
                }

                const parts = date.split('-');

                const year = Number(parts[0]);
                const month = Number(parts[1]);
                const day = Number(parts[2]);

                return new Date(year, month - 1, day);
            },
            set(date) {
                if (!date) {
                    return;
                }

                const year = date.getFullYear();
                const month = String(date.getMonth() + 1).padStart(2, '0');
                const day = String(date.getDate()).padStart(2, '0');

                this.session.target.date = `${year}-${month}-${day}`;
            }
        },
        time: {
            get() {
                const time = this.session.target.time;

                if (!time) {
                    return null;
                }

                const parts = time.split(':');

                const hours = parts[0];
                const minutes = parts[1];

                const date = new Date();

                date.setHours(hours, minutes, 0, 0);

                return date;
            },
            set(time) {
                if (!time) {
                    return;
                }

                const hours = String(time.getHours()).padStart(2, '0');
                const minutes = String(time.getMinutes()).padStart(2, '0');

                this.session.target.time = `${hours}:${minutes}`;
            }
        },
        canApply() {
            const requiredFields = [
                'movie',
                'location',
                'date',
                'time'
            ];

            for (let requiredField of requiredFields) {
                if (_.isEmpty(this.session.target[requiredField])) {
                    return false;
                }
            }

            return true;
        }
    },
    methods: {
        async open(mode, session, callback) {
            await this.load();

            this.mode = mode;

            if (mode === 'create') {
                this.session.target = this.getDefault();
            } else {
                this.session.source = session;
                this.session.target = _.cloneDeep(session);
            }

            this.callback = callback;
            this.visible = true;
        },
        async load() {
            this.movies = [];
            this.locations = [];

            const promises = [];

            promises.push(MovieService.getAll());
            promises.push(LocationService.getAll());

            const responses = await Promise.all(promises);

            this.movies = responses[0];
            this.locations = responses[1];
        },
        getDefault() {
            return {
                movie: null,
                location: null,
                date: null,
                time: null,
                threeD: false,
                subtitled: false
            };
        },
        close() {
            this.visible = false;
        },
        apply() {
            this.callback(this.session.target);
            this.close();
        },
        toOption(item) {
            let key = 'name';

            if (!Object.keys(item).includes(key)) {
                key = 'title';
            }

            const label = item[key];

            const value = {
                id: item.id
            };

            value[key] = label;

            return { label, value };
        }
    }
};
</script>

<style lang="scss" scoped>
.input {
    display: flex;
    flex-direction: column;
    gap: 5px;
    margin-bottom: 1rem;

    & > label {
        font-weight: bold;
    }
}
</style>